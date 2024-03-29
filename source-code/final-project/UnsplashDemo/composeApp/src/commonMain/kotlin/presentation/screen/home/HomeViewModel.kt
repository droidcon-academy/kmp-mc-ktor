package presentation.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import api.UnsplashApi
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.Photos
import domain.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ScreenModel {
    private var _data: MutableState<RequestState<Photos>> = mutableStateOf(RequestState.Idle)
    val data: State<RequestState<Photos>> = _data

    init {
        screenModelScope.launch(Dispatchers.Main) {
            UnsplashApi().getPhotos().collect { requestState ->
                _data.value = requestState
            }
        }
    }
}