package io.smoothbear.socketio.misc

class CompositeIterable<T> (
    var iterables: Array<Iterable<T>>?,
    var iterablesList: List<Iterable<T>>?
) : Iterable<T> {

    constructor(iterable: CompositeIterable<T>) : this(iterable.iterables, iterable.iterablesList)

    override fun iterator(): Iterator<T> {
        val iterators = arrayListOf<Iterator<T>>()
        if (iterables != null) {
            for (iterable in iterables!!) {
                iterators.add(iterable.iterator())
            }
        } else {
            for (iterable in iterablesList!!) {
                iterators.add(iterable.iterator())
            }
        }

        return CompositeIterator(iterators.iterator())
    }
}