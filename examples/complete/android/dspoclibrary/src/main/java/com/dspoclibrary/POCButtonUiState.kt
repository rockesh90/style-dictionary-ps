package com.dspoclibrary

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class POCButtonUiState(
  val backgroundColor: Color = Color(android.graphics.Color.parseColor("#419488")),
  val buttonTextColor: Color = Color(android.graphics.Color.parseColor("#419488")),
  val buttonBackgroundColor: Color = Color.White,
  val labelTextColor: Color = Color.White,
  val mediumFontSize: TextUnit = 16.sp,
  val largeFontSize: TextUnit = 22.sp,
  val loadStyle: () -> Unit = {},
  val error: String? = null,
)

data class ErrorState(
  val errorMessage: String? = null
)
