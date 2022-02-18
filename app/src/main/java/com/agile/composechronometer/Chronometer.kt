package com.agile.composechronometer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agile.composechronometer.ui.theme.Purple700
import java.time.Duration

@Composable
fun Chronometer(mainViewModel: MainViewModel = viewModel()) {
    val state = mainViewModel.viewState.observeAsState(initial = TimerUiModel()).value
    val remainingDuration = state.remainingDuration

    Clock(state.percentage()){ TimerText(duration = remainingDuration) }
    FooterButtons(
        state,
        onResetClicked = { mainViewModel.onResetPressed() },
        onStartClicked = { mainViewModel.onStartPressed() },
        onStopClicked = { mainViewModel.onStopPressed() })
}

@Composable
private fun Clock(progress: Float, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 36.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Default config
        val drawStyle = remember { Stroke(width = 24.dp.value, cap = StrokeCap.Round) }
        val defaultBrushColor = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        val defaultBrush = remember { SolidColor(defaultBrushColor) }
        // progress color
        val progressColor = Purple700
        val progressBrush = remember {
            SolidColor(progressColor)
        }

        val animateCurrentProgress = animateFloatAsState(
            targetValue = progress,
            animationSpec = tween(durationMillis = 100, easing = LinearEasing)
        )

        val progressDegrees = animateCurrentProgress.value * 360f

        Box {
            Canvas(modifier = Modifier.size(200.dp), onDraw = {
                drawArc(
                    brush = defaultBrush,
                    startAngle = 90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = drawStyle
                )
                drawArc(
                    brush = progressBrush,
                    startAngle = 270f,
                    sweepAngle = progressDegrees,
                    useCenter = false,
                    style = drawStyle
                )
            })
            Box(modifier = Modifier.align(Alignment.Center)) {
                content()
            }
        }
    }
}

@Composable
private fun TimerText(duration: Duration) {
    Text(text = buildAnnotatedString {
        append(duration.toMinutes().toString())
        append(":")
        append(duration.getPartSeconds().toString())
    }, fontSize = 32.sp)
}

@Composable
private fun FooterButtons(
    state: TimerUiModel,
    onResetClicked: () -> Unit,
    onStartClicked: () -> Unit,
    onStopClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        val timerBtnText =
            if (state.isStoppedOrFinished()) "Start" else "Stop"

        Row {
            Button(
                onClick = { onResetClicked() },
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text("Reset")
            }
            Button(
                onClick = { if (state.isStoppedOrFinished()) onStartClicked() else onStopClicked() },
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(timerBtnText)
            }
        }
    }
}
