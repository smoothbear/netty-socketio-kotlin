package io.smoothbear.socketio

import io.netty.handler.codec.http.HttpHeaders
import java.io.Serializable
import java.net.InetSocketAddress

class HandShakeData (
    val httpHeaders: HttpHeaders,
    val urlParams: Map<String, List<String>>,
    val address: InetSocketAddress,
    val local: InetSocketAddress?,
    val url: String,
    val xdomain: Boolean
) : Serializable {

    fun getSingleUrlParam(name: String): String? {
        val values = urlParams[name]
        if (values != null && values.size == 1)
            return values.iterator().next()

        return null
    }
}