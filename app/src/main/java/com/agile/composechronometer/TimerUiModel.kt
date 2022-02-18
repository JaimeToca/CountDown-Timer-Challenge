package com.agile.composechronometer

import java.time.Duration

data class TimerUiModel(
    val state: TimerState = TimerState.STOPPED,
    val duration: Duration = Duration.ofSeconds(60L),
    val remainingDuration: Duration = duration
) {
    fun isStoppedOrFinished(): Boolean {
        return (state == TimerState.STOPPED || state == TimerState.FINISHED)
    }

    fun percentage(): Float {
            return (remainingDuration.toMillis().toFloat() / duration.toMillis().toFloat())
    }
}

enum class TimerState { RUNNING, STOPPED, FINISHED }