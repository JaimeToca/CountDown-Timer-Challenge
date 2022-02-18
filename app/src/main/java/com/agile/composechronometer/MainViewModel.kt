package com.agile.composechronometer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.Duration

class MainViewModel : ViewModel() {

    private var timer: CountDownTimer? = null
    private val _viewState = MutableLiveData<TimerUiModel>()
    val viewState: LiveData<TimerUiModel>
        get() = _viewState

    init {
        _viewState.value = TimerUiModel()
    }

    fun onStartPressed() {
        if (_viewState.value!!.remainingDuration.toMillis() == 0L){
            _viewState.value = TimerUiModel()
        }

        timer = object : CountDownTimer(_viewState.value!!.remainingDuration.toMillis(), 100) {
            override fun onTick(millisUntilFinished: Long) {
                _viewState.value = _viewState.value!!.copy(
                    state = TimerState.RUNNING,
                    duration = _viewState.value!!.duration,
                    remainingDuration = Duration.ofMillis(millisUntilFinished)
                )
            }

            override fun onFinish() {
                _viewState.value = _viewState.value!!.copy(
                    state = TimerState.FINISHED,
                    duration = Duration.ofSeconds(0L),
                    remainingDuration = Duration.ofSeconds(0L)
                )
            }
        }.start()
    }

    fun onStopPressed() {
        timer?.cancel()
        _viewState.value = _viewState.value!!.copy(
            state = TimerState.STOPPED,
            duration = _viewState.value!!.duration,
            remainingDuration = _viewState.value!!.remainingDuration
        )
    }

    fun onResetPressed() {
        timer?.cancel()
        _viewState.value = TimerUiModel(
            state = TimerState.FINISHED,
            Duration.ofSeconds(60L),
            Duration.ofSeconds(60L)
        )
    }
}