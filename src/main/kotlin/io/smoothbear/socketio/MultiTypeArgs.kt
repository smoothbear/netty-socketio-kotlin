package io.smoothbear.socketio

class MultiTypeArgs (
    val args: List<Any>
) : Iterable<Any> {

    fun isEmpty(): Boolean = size() == 0
    fun size(): Int = args.size
    fun first(): Any = args[0]
    fun second(): Any = args[1]

    fun get(index: Int): Any? {
        if (size() <= index)
            return null

        return args[index]
    }

    override fun iterator(): Iterator<Any> = args.iterator()
}