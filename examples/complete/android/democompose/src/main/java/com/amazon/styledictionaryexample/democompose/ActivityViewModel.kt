package com.amazon.styledictionaryexample.democompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazon.styledictionaryexample.democompose.network.DemoComposeService
import com.dspoclibrary.POCButtonStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import utils.StyleDictionaryHelper

class ActivityViewModel :
  ViewModel() {
  private val _uiState = MutableStateFlow(
    ActivityUiState(
      loadStyle = { loadJson() },
    ),
  )
  val uiState = _uiState.asStateFlow()

  private val demoComposeService: DemoComposeService = Retrofit
    .Builder().baseUrl(
      "https://raw.githubusercontent.com/alereyes2/style-dictionary-ps/main/examples/complete/android/styledictionary/src/main/",
    ).addConverterFactory(
      ScalarsConverterFactory.create(),
    ).build().create(DemoComposeService::class.java)

  init {
    loadJson()
  }

  private fun loadJson() {
    viewModelScope.launch {
      val response = demoComposeService.retrieveStyleJson()
      StyleDictionaryHelper.updateJson(response.body()!!)
      try {
        _uiState.value = _uiState.value.copy(
          POCButtonStyle(
            buttonBackgroundColor = StyleDictionaryHelper.loadButtonBackgroundColor(),
            backgroundColor = StyleDictionaryHelper.loadBackgroundColor(),
            mediumFontSize = StyleDictionaryHelper.loadMediumFontSize(),
            largeFontSize = StyleDictionaryHelper.loadLargeFontSize(),
            labelTextColor = StyleDictionaryHelper.labelTextColor(),
            buttonTextColor = StyleDictionaryHelper.loadBackgroundColor(),
          ),
        )
      } catch (e: Exception) {
        _uiState.value = _uiState.value.copy(
          error = "Unable to load Style",
        )
      }
    }
  }

}
