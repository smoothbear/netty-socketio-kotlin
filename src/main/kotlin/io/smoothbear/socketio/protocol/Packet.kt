package io.smoothbear.socketio.protocol

import io.netty.buffer.ByteBuf
import io.smoothbear.socketio.namespace.Namespace
import java.io.Serializable

class Packet (
    val type: PacketType
) : Serializable {

    var ackId: Long? = null
    var subType: PacketType? = null
    var name: String? = null
    var nsp: String = Namespace.DEFAULT_NAME
    var data: Any? = null

    var dataSource: ByteBuf? = null
    var attachmentsCount: Int = 0
    var attachments: List<ByteBuf> = emptyList()

    fun withNsp(namespace: String): Packet {
        if (this.nsp.equals(namespace, ignoreCase = true))
            return this

        val packet: Packet = Packet(this.type)
        packet.ackId = this.ackId
        packet.data = this.data
        packet.dataSource = this.dataSource
        packet.name = this.name
        packet.subType = this.subType
        packet.nsp = this.nsp
        packet.attachments = this.attachments
        packet.attachmentsCount = this.attachmentsCount

        return packet
    }

    fun isAckRequested(): Boolean {
        return ackId != null
    }

    fun initAttachments(attachmentsCount: Int) {
        this.attachmentsCount = attachmentsCount
        this.attachments = ArrayList(attachmentsCount)
    }

    fun addAttachments(attachment: ByteBuf) {
        if (this.attachments.size < attachmentsCount) {
            this.attachments += attachment
        }
    }

    // Method for Attachments
    fun hasAttachments(): Boolean = attachmentsCount != 0
    fun isAttachmentsLoaded(): Boolean = this.attachments.size == attachmentsCount

    override fun toString(): String {
        return "Packet [type=$type, ackId=$ackId]";
    }
}