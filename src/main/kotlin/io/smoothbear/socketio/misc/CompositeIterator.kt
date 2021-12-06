package io.smoothbear.socketio.misc

class CompositeIterator<T> (
    private val listIterator: Iterator<Iterator<T>>
) : Iterator<T> {

    private var currentIterator: Iterator<T>? = null

    override fun hasNext(): Boolean {
        if (currentIterator == null || !currentIterator!!.hasNext()) {
            while (listIterator.hasNext()) {
                val iterator = listIterator.next()
                if (iterator.hasNext()) {
                    currentIterator = iterator
                    return true
                }
            }
            return false
        }
        return currentIterator!!.hasNext()
    }

    override fun next(): T {
        hasNext()
        return currentIterator!!.next()
    }
}