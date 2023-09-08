package jp.co.toukei.log.trustar.enum

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@JvmInline
@Immutable
value class BinStatusEnum(val value: Int) {

    companion object {

        @Stable
        val New = BinStatusEnum(0)

        @Stable
        val Working = BinStatusEnum(1)

        @Stable
        val Finished = BinStatusEnum(2)

        @Stable
        val Unknown = BinStatusEnum(-1)

    }

    fun isNew(): Boolean = value == 0

    fun started(): Boolean = value > 0

    fun isWorking(): Boolean = value == 1

    fun isFinished(): Boolean = value == 2
}

