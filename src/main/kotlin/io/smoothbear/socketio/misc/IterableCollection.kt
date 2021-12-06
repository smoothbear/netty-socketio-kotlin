package io.smoothbear.socketio.misc

class IterableCollection<T> (
    private val iterable: CompositeIterable<T>
) : AbstractCollection<T>() {

    public constructor(iterable: Iterable<T>) : this(CompositeIterable(arrayOf(iterable), null))

    override fun iterator(): Iterator<T> = CompositeIterable(iterable).iterator()

    override val size: Int
        get() {
            val iterator = CompositeIterable(iterable).iterator()
            var count: Int = 0
            while (iterator.hasNext()) {
                iterator.next()
                count++
            }
            return count
        }
}