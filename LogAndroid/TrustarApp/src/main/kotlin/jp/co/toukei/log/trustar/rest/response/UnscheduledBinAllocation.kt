package jp.co.toukei.log.trustar.rest.response

import annotation.Keep
import annotation.Para
import io.reactivex.rxjava3.core.Single

class UnscheduledBinAllocation @Keep constructor(
        @Para("AllocationNo") @JvmField val allocationNo: String?,
        @Para("PostResponse") @JvmField val postResponse: StateResult
) {
    fun toSingle(): Single<String> {
        return if (postResponse.result) {
            val a = allocationNo
            if (a == null) Single.error(ResponseException())
            else Single.just(a)
        } else Single.error(ResponseException(postResponse.message))
    }
}
