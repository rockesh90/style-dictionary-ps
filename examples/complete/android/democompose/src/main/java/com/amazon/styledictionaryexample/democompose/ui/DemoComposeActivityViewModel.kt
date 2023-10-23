package com.amazon.styledictionaryexample.democompose.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amazon.styledictionaryexample.democompose.DemoComposeApplication
import com.amazon.styledictionaryexample.democompose.network.DemoComposeService
import com.amazon.styledictionaryexample.democompose.ui.model.DemoComposeUiState
import com.amazon.styledictionaryexample.democompose.ui.model.ErrorState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import utils.StyleDictionaryHelper

class DemoComposeActivityViewModel(
  application: DemoComposeApplication,
  private val demoComposeService: DemoComposeService,
) :
  AndroidViewModel(application = application) {
  private val _uiState = MutableSharedFlow<DemoComposeUiState>()
  val uiState = _uiState

  private val _errorSate = MutableSharedFlow<ErrorState>(
    replay = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST,
    extraBufferCapacity = 10,
  )
  val errorState = _errorSate.asSharedFlow()

  init {
    loadJson()
  }

  fun loadJson() {
    viewModelScope.launch {
      val response = demoComposeService.retrieveStyleJson()
      StyleDictionaryHelper.updateJson(response.body()!!)
      try {
        _uiState.emit(
          demoComposeUiStateWithValues(),
        )
      } catch (e: Exception) {
        _uiState.emit(
          DemoComposeUiState(
            loadStyle = { loadJson() },
            error = "Error loading style",
          ),
        )
      }
    }
  }

  private fun demoComposeUiStateWithValues() = DemoComposeUiState(
    buttonBackgroundColor = StyleDictionaryHelper.loadButtonBackgroundColor(),
    backgroundColor = StyleDictionaryHelper.loadBackgroundColor(),
    mediumFontSize = StyleDictionaryHelper.loadMediumFontSize(),
    largeFontSize = StyleDictionaryHelper.loadLargeFontSize(),
    labelTextColor = StyleDictionaryHelper.labelTextColor(),
    buttonTextColor = StyleDictionaryHelper.loadBackgroundColor(),
    loadStyle = { loadJson() },
  )

  private fun reloadJson() {
    loadJson()
  }
}
