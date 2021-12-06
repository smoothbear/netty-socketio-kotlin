package io.smoothbear.socketio

import io.smoothbear.socketio.listener.DefaultExceptionListener
import io.smoothbear.socketio.protocol.JsonSupport
import io.smoothbear.socketio.store.StoreFactory
import java.io.InputStream
import javax.net.ssl.KeyManagerFactory


class Configuration (
    conf: Configuration
) {

    var exceptionListener = DefaultExceptionListener()

    var context = "/socket.io"

    var transports = listOf(Transport.WEBSOCKET, Transport.POLLING)

    var bossThreads = 0
    var workerThreads = 0
    var useLinuxNativeEpoll: Boolean = false

    var allowCustomRequests = false

    var upgradeTimeout = 10000
    var pingTimeout = 60000
    var pingInterval = 25000
    var firstDataTimeout = 5000

    var maxHttpContentLength = 64 * 1024
    var maxFramePayloadLength = 64 * 1024

    var packagePrefix: String? = null
    var hostname: String? = null
    var port = -1

    var sslProtocol = "TLSv1"

    var keyStoreFormat = "JKS"
    var keyStore: InputStream? = null
    var keyStorePassword: String? = null

    var trustStoreFormat = "JKS"
    var trustStore: InputStream? = null
    var trustStorePassword: String? = null

    var keyManagerFactoryAlgorithm = KeyManagerFactory.getDefaultAlgorithm()

    var preferDirectBuffer = true

    var socketConfig = SocketConfig()

    var storeFactory: StoreFactory = MemoryStoreFactory()

    var jsonSupport: JsonSupport? = null

    var authorizationListener: AuthorizationListener = SuccessAuthorizationListener()

    var ackMode = AckMode.AUTO_SUCCESS_ONLY

    var addVersionHeader = true

    var origin: String? = null

    var httpCompression = true

    var websocketCompression = true

    var randomSession = false

    init {
        bossThreads = conf.bossThreads
        workerThreads = conf.workerThreads

    }
}