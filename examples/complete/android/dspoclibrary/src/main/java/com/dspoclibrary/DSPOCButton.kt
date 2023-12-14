package com.dspoclibrary

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DSPOCButton(
  title: String = "",
  onClick: () -> Unit = {},
  buttonUiState: POCButtonStyle = POCButtonStyle(),
) {
  Button(
    colors = ButtonDefaults.buttonColors(
      containerColor = buttonUiState.buttonBackgroundColor,
    ),
    onClick = {
      onClick()
    },
  ) {
    Text(text = title, color = buttonUiState.buttonTextColor)
  }
}
