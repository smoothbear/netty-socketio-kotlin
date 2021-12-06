package io.smoothbear.socketio.protocol

import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import io.smoothbear.socketio.AckCallback


interface JsonSupport {

    fun readAckArgs(src: ByteBufInputStream, callback: AckCallback<*>): AckArgs

    fun <T> readValue(namespaceName: String, src: ByteBufInputStream, valueType: Class<T>): T

    fun writeValue(out: ByteBufOutputStream, value: Any)

    fun addEventMapping(namespaceName: String, eventName: String, vararg eventClass: Class<*>)

    fun removeEventMapping(namespaceName: String, eventName: String)

    fun getArrays(): List<ByteArray>?

}