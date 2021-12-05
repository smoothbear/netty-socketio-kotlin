package io.smoothbear.socketio.handler

import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.util.internal.PlatformDependent
import io.smoothbear.socketio.Configuration
import io.smoothbear.socketio.DisconnectableHub
import io.smoothbear.socketio.HandShakeData
import io.smoothbear.socketio.Transport
import io.smoothbear.socketio.ack.AckManager
import io.smoothbear.socketio.message.OutPacketMessage
import io.smoothbear.socketio.namespace.Namespace
import io.smoothbear.socketio.protocol.Packet
import io.smoothbear.socketio.protocol.PacketType
import io.smoothbear.socketio.scheduler.CancelableScheduler
import io.smoothbear.socketio.scheduler.SchedulerKey
import io.smoothbear.socketio.scheduler.SchedulerKey.Type
import io.smoothbear.socketio.store.StoreFactory
import io.smoothbear.socketio.transport.NamespaceClient
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.HashMap

class ClientHead (
    val sessionId: UUID,
    private val ackManager: AckManager,
    private val disconnectable: DisconnectableHub,
    private val storeFactory: StoreFactory,
    val handshakeData: HandShakeData,
    val clientsBox: ClientsBox,
    val disconnectScheduler: CancelableScheduler,
    val configuration: Configuration,

    // Read and write Main memory
    @Volatile var currentTransport: Transport
) {

    private val disconnected: AtomicBoolean = AtomicBoolean()
    private val namespaceClients: MutableMap<Namespace, NamespaceClient> = PlatformDependent.newConcurrentHashMap()
    private val channels: MutableMap<Transport, TransportState> = HashMap(2)

    init {
        channels[Transport.POLLING] = TransportState()
        channels[Transport.WEBSOCKET] = TransportState()
    }

    companion object {
        @JvmField
        val logger = LoggerFactory.getLogger(ClientHead::class.java)
    }

    fun bindChannel(channel: Channel, transport: Transport) {
        val state = channels[transport]

        val prevChannel = state.update(channel)
        if (prevChannel != null)
            clientsBox.remove(prevChannel)

        clientsBox.add(channel, this)

        sendPackets(transport, channel)
    }

    fun releasePollingChannel(channel: Channel) {
        val state = channels[Transport.POLLING] ?: return

        if (channels == state.channel) {
            clientsBox.remove(channel)
            state.update(null)
        }
    }

    fun getOrigin(): String {
        return handshakeData.httpHeaders.get(HttpHeaderNames.ORIGIN)
    }

    fun send(packet: Packet): ChannelFuture? {
        return send(packet, currentTransport)
    }

    fun cancelPingTimeout() {
        val key = SchedulerKey(Type.PING_TIMEOUT, sessionId)
        disconnectScheduler.cancel(key)
    }

    fun schedulePingTimeout() {
        val key = SchedulerKey(Type.PING_TIMEOUT, sessionId)
        disconnectScheduler.schedule(key, configuration.pingTimeout + configuration.pingInterval, TimeUnit.MILLISECONDS) {
            val client = clientsBox.get(sessionId)
            if (client != null) {
                client.disconnect()
                logger.debug("$sessionId removed due to ping timeout")
            }
        }
    }

    fun send(packet: Packet, transport: Transport): ChannelFuture? {
        val state = channels[transport]
        state.packetsQueue.add(packet)

        val channel = state?.channel
        if (channel == null
            || (transport == Transport.POLLING && channel.attr(EncoderHandler.WRITE_ONCE).get() != null)) {
            return null
        }

        return sendPackets(transport, channel)
    }

    private fun sendPackets(transport: Transport, channel: Channel): ChannelFuture {
        return channel.writeAndFlush(OutPacketMessage(this, transport))
    }

    fun onChannelDisconnect() {
        cancelPingTimeout()

        disconnected.set(true)
        for (client in namespaceClients.values)
    }

    fun disconnect() {
        send(Packet(PacketType.DISCONNECT))?.addListener(ChannelFutureListener.CLOSE)

        onChannelDisconnect()
    }
}