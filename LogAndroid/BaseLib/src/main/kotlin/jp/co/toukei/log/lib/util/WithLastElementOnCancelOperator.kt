package jp.co.toukei.log.lib.util

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableOperator
import io.reactivex.rxjava3.internal.subscriptions.SubscriptionHelper
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class WithLastElementOnCancelOperator<T : Any>(
    private val onCancel: (T?) -> Unit,
) : FlowableOperator<T, T> {
    override fun apply(subscriber: Subscriber<in T>): Subscriber<in T> {
        return S(subscriber, onCancel)
    }

    private class S<T>(
        private val downstream: Subscriber<in T>,
        private val onCancel: (T?) -> Unit,
    ) : Subscriber<T>, Subscription {

        private var upstream: Subscription? = null

        private var lastElement: T? = null

        override fun cancel() {
            upstream?.let {
                upstream = null
                it.cancel()
                onCancel(lastElement)
                lastElement = null
            }
        }

        override fun request(n: Long) {
            upstream?.request(n)
        }

        override fun onSubscribe(s: Subscription) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                upstream = s
                downstream.onSubscribe(this)
            }
        }

        override fun onError(t: Throwable?) {
            downstream.onError(t)
        }

        override fun onComplete() {
            downstream.onComplete()
        }

        override fun onNext(t: T) {
            lastElement = t
            downstream.onNext(t)
        }
    }
}


fun <T : Any> Flowable<T>.withLastElementOnCancel(
    onCancel: (T?) -> Unit,
): Flowable<T> {
    return lift(WithLastElementOnCancelOperator(onCancel))
}
