package com.dspoclibrary

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun DSPOCButton(
  pocButtonViewModel: POCButtonViewModel = POCButtonViewModel(),
  title: String = "",
  reload: Boolean = false,
  onClick: () -> Unit = {},
) {
  val uiState = pocButtonViewModel.uiState
    .collectAsState(initial = POCButtonUiState(loadStyle = { pocButtonViewModel.loadJson() })).value
  Button(
    colors = ButtonDefaults.buttonColors(
      containerColor = uiState.buttonBackgroundColor,
    ),
    onClick = {
      onClick()
    },
  ) {
    Text(text = title, color = uiState.buttonTextColor)
  }
  if (reload) {
    LaunchedEffect(null) {
      pocButtonViewModel.loadJson()
    }
  }
}
