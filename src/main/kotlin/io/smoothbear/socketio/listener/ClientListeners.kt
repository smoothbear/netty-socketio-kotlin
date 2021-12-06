package io.smoothbear.socketio.listener

import kotlin.reflect.KClass

interface ClientListeners {

    fun addMultiTypeEventListener(eventName: String, listener: MultiTypeEventListener, vararg eventClass: KClass<*>)

    fun <T> addEventListener(eventName: String, eventClass: KClass<*>, listener: DataListener<Any?>)

    fun addEventInterceptor(eventInterceptor: EventInterceptor)

    fun addDisconnectListener(listener: DisconnectListener)

    fun addConnectListener(listener: ConnectListener)

    fun addPingListener(listener: PingListener)

    fun addListeners(listeners: Any)

    fun addListeners(listeners: Any, listenersClass: KClass<*>)

    fun removeAllListeners(eventName: String)

}