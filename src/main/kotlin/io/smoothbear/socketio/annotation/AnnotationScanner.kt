package io.smoothbear.socketio.annotation

import io.smoothbear.socketio.namespace.Namespace
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

interface AnnotationScanner {
    fun getScanAnnotation(): KClass<out Annotation>
    fun addListener(namespace: Namespace, obj: Any, method: KFunction<*>, annotation: Annotation)
    fun validate(method: KFunction<*>, clz: KClass<*>)
}