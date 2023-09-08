package jp.co.toukei.log.trustar.enum

import androidx.compose.runtime.Immutable

@JvmInline
@Immutable
value class NoticeUseCase private constructor(val value: Int) {

    companion object {
        val Logout = NoticeUseCase(1)
        val Restart = NoticeUseCase(2)
        val None = NoticeUseCase(-1)
    }

    fun clickable(): Boolean {
        return this != None
    }
}
