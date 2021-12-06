package io.smoothbear.socketio.annotation

import org.slf4j.LoggerFactory
import java.util.*

class ScannerEngine {

    companion object {
        @JvmField
        val logger = LoggerFactory.getLogger(ScannerEngine::class.java)

        @JvmField
        val annotations = listOf(OnConnectScanner::class)
    }
}