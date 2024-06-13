package com.amazon.styledictionaryexample.democompose

import com.dspoclibrary.POCButtonStyle

data class ActivityUiState(
  val buttonStyle: POCButtonStyle = POCButtonStyle(),
  val loadStyle: () -> Unit = {},
  val error: String? = null,
)
