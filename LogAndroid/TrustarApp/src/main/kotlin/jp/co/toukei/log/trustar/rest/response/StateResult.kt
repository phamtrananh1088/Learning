package jp.co.toukei.log.trustar.rest.response

import annotation.Keep
import annotation.Para
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import third.Result
import java.util.*

class StateResult @Keep constructor(
    @Para("result") @JvmField val result: Boolean,
    @Para("message") @JvmField val message: String?
) {
    fun <T : Any> toResult(value: T): Result<T> {
        return if (result) Result.Value(value)
        else Result.Error(ResponseException(message))
    }

    fun toSingleMessage(): Single<String> {
        return if (result && message != null) Single.just(message)
        else Single.error(ResponseException(message ?: "message is null."))
    }

    fun toSingleOptionalMessage(): Single<Optional<String>> {
        return if (result) Single.just(Optional.ofNullable(message))
        else Single.error(ResponseException(message))
    }

    fun toCompletable(): Completable {
        return if (result) Completable.complete()
        else Completable.error(ResponseException(message))
    }
}
