package jp.co.toukei.log.trustar.enum

import androidx.compose.runtime.Immutable

@JvmInline
@Immutable
value class WorkStatusEnum(val value: Int) {

    companion object {

        val New = WorkStatusEnum(0)

        val Moving = WorkStatusEnum(1)

        val Working = WorkStatusEnum(2)

        val Finished = WorkStatusEnum(3)

        val Unknown = WorkStatusEnum(-1)
    }

    fun isNew(): Boolean = value == 0

    fun isMoving(): Boolean = value == 1

    fun isWorking(): Boolean = value == 2

    fun isNotWorking(): Boolean = !isWorking()

    fun isWorkingOrMoving(): Boolean = isMoving() || isWorking()

    fun isFinished(): Boolean = value == 3
}

