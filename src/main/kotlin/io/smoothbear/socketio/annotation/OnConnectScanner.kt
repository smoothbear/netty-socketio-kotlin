package io.smoothbear.socketio.annotation

import io.smoothbear.socketio.SocketIOClient
import io.smoothbear.socketio.handler.SocketIOException
import io.smoothbear.socketio.listener.ConnectListener
import io.smoothbear.socketio.namespace.Namespace
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class OnConnectScanner : AnnotationScanner {

    override fun getScanAnnotation(): KClass<out Annotation> {
        return OnConnect::class
    }

    override fun addListener(namespace: Namespace, obj: Any, method: KFunction<*>, annotation: Annotation) {
        namespace.addConnectListener(object : ConnectListener {
            override fun onConnect(client: SocketIOClient) {
                try {
                    method.call(obj, client)
                } catch (e: Exception) {
                    throw SocketIOException(e)
                }
            }
        })
    }

    override fun validate(method: KFunction<*>, clz: KClass<*>) {
        if (method.typeParameters.size != 1) {
            throw IllegalArgumentException("Wrong OnConnect listener signature: " + clz + "." + method.name);
        }

        var valid = false
        for (eventType in method.typeParameters) {
            if (eventType == SocketIOClient::class) {
                valid = true
            }
        }

        if (!valid) {
            throw IllegalArgumentException("Wrong OnConnect listener signature: " + clz + "." + method.name)
        }
    }
}