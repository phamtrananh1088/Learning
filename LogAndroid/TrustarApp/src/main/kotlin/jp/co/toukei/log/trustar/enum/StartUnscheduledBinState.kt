package jp.co.toukei.log.trustar.enum

import androidx.compose.runtime.Immutable

@Immutable
sealed class StartUnscheduledBinState(
    val detectHasWorking: Boolean,
) {
    class TokenLoading : StartUnscheduledBinState(true)
    class GetTokenError(val err: Throwable) : StartUnscheduledBinState(true)
    class WorkingBinExists : StartUnscheduledBinState(true)
    class Ready(val token: String) : StartUnscheduledBinState(true)
    class Sending : StartUnscheduledBinState(false)
    class AddFailed : StartUnscheduledBinState(false)
    class Added(val allocationNo: String) : StartUnscheduledBinState(false)
    class AddedButReloadFailed(val allocationNo: String) : StartUnscheduledBinState(false)
}
