package util

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import domain.RequestState

@Composable
fun <T> RequestState<T>.DisplayResult(
    onIdle: (@Composable () -> Unit)? = null,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (T) -> Unit,
    onError: @Composable (String) -> Unit,
) {
    AnimatedContent(
        targetState = this,
        transitionSpec = {
            fadeIn(tween(durationMillis = 300)) togetherWith
                    fadeOut(tween(durationMillis = 300))
        },
        label = "Content Animation"
    ) { state ->
        when (state) {
            is RequestState.Idle -> {
                onIdle?.invoke()
            }

            is RequestState.Loading -> {
                onLoading()
            }

            is RequestState.Success -> {
                onSuccess(state.getSuccessData())
            }

            is RequestState.Error -> {
                onError(state.getErrorMessage())
            }
        }
    }
}