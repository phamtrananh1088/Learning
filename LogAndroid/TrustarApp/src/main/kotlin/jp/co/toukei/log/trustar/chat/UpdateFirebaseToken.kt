package jp.co.toukei.log.trustar.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.processors.PublishProcessor
import jp.co.toukei.log.lib.delayedRetry
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.toResult
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.rest.post.DefaultPostContent
import jp.co.toukei.log.trustar.toCompletable
import third.Result
import java.util.concurrent.TimeUnit

object UpdateFirebaseToken {

    private fun getToken(invalid: Boolean): Single<String> {
        return Single.create { e ->
            val firebase = FirebaseMessaging.getInstance()
            if (invalid)
                firebase.deleteToken()
            firebase.token
                .addOnSuccessListener(e::onSuccess)
                .addOnFailureListener(e::onError)
        }.observeOnIO().subscribeOnIO()
    }

    private fun invalidToken(): Completable {
        return getToken(true).ignoreElement()
    }

    private fun sendToken(delayedSendSec: Long): Completable {
        return getToken(false)
            .delay(delayedSendSec, TimeUnit.SECONDS)
            .flatMap {
                Current.chatApi.registerFirebaseToken(DefaultPostContent.token(it))
            }
            .toCompletable()
    }

    private val request = PublishProcessor.create<Boolean>()

    @Volatile
    private var tokenGenerated = false

    @JvmField
    val requestState: LiveData<Result<Unit>> = request
        .onBackpressureLatest()
        .switchMapSingle {
            val c = if (it || !tokenGenerated) {
                invalidToken().andThen(Completable.defer {
                    tokenGenerated = true
                    sendToken(2)
                })
            } else {
                sendToken(0)
            }
            c.toSingleDefault(Unit)
                .delayedRetry(3, 10, TimeUnit.SECONDS)
                .toResult()
        }
        .publish()
        .autoConnect(0)
        .toLiveData()

    fun updateToken() {
        request.onNext(false)
    }

    fun newToken() {
        tokenGenerated = false
        request.onNext(true)
    }
}
