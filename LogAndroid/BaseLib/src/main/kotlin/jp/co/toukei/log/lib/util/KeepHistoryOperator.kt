package jp.co.toukei.log.lib.util

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableOperator
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.LinkedList

class KeepHistoryOperator<T : Any>(
    private val dropOldWhile: (current: T, item: T) -> Boolean,
) : FlowableOperator<Pair<List<T>, List<T>>, T> {

    override fun apply(subscriber: Subscriber<in Pair<List<T>, List<T>>>): Subscriber<in T> {
        return S(dropOldWhile, subscriber)
    }

    private class S<T : Any>(
        private val dropOldWhile: (T, T) -> Boolean,
        private val downstream: Subscriber<in Pair<List<T>, List<T>>>,
    ) : Subscriber<T> {

        override fun onSubscribe(s: Subscription?) {
            downstream.onSubscribe(s)
        }

        override fun onError(t: Throwable?) {
            downstream.onError(t)
        }

        override fun onComplete() {
            downstream.onComplete()
        }

        private var l = LinkedList<T>()

        private val emptyList = emptyList<T>()

        override fun onNext(t: T) {
            l.addLast(t)

            val f = l.peekFirst()
            val d = if (f != null && dropOldWhile(t, f)) {
                val drop = LinkedList<T>()
                do {
                    drop += l.removeFirst()
                    val e = l.peekFirst()
                } while (e != null && dropOldWhile(t, e))
                drop
            } else {
                emptyList
            }
            downstream.onNext(d to l.toList())
        }
    }
}

fun <T : Any> Flowable<T>.keepHistoryUntil(f: (current: T, item: T) -> Boolean): Flowable<Pair<List<T>, List<T>>> {
    return lift(KeepHistoryOperator(f))
}
