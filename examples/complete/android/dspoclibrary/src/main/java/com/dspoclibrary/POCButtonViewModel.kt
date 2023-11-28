package com.dspoclibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dspoclibrary.network.DemoComposeService
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import utils.StyleDictionaryHelper

class POCButtonViewModel :
  ViewModel() {
  private val _uiState = MutableSharedFlow<POCButtonUiState>()
  val uiState = _uiState

  private val demoComposeService: DemoComposeService = Retrofit
    .Builder().baseUrl("https://raw.githubusercontent.com/alereyes2/style-dictionary-ps/main/examples/complete/android/styledictionary/src/main/").addConverterFactory(
      ScalarsConverterFactory.create(),
    ).build().create(DemoComposeService::class.java)

  private val _errorSate = MutableSharedFlow<ErrorState>(
    replay = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST,
    extraBufferCapacity = 10,
  )
  val errorState = _errorSate.asSharedFlow()

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
          POCButtonUiState(
            loadStyle = { loadJson() },
            error = "Error loading style",
          ),
        )
      }
    }
  }

  private fun demoComposeUiStateWithValues() = POCButtonUiState(
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
