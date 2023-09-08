package jp.co.toukei.log.lib.util

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableOperator
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class FilterWithLastOperator<T : Any>(
    private val filter: (upStreamLast: T?, downStreamLast: T?, current: T) -> Boolean,
) : FlowableOperator<T, T> {

    override fun apply(subscriber: Subscriber<in T>): Subscriber<in T> {
        return S(filter, subscriber)
    }

    private class S<T : Any>(
        private val filter: (upStreamPrevious: T?, downStreamPrevious: T?, current: T) -> Boolean,
        private val downstream: Subscriber<in T>,
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

        private var upStreamLast: T? = null
        private var downStreamLast: T? = null

        override fun onNext(t: T) {
            val u = upStreamLast
            upStreamLast = t
            if (filter(u, downStreamLast, t)) {
                downstream.onNext(t)
                downStreamLast = t
            }
        }
    }
}

fun <T : Any> Flowable<T>.filterWithLast(
    filter: (upStreamLast: T?, downStreamLast: T?, current: T) -> Boolean,
): Flowable<T> {
    return lift(FilterWithLastOperator(filter))
}
