package com.amazon.styledictionaryexample.democompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amazon.styledictionaryexample.democompose.ui.theme.AndroidTheme
import com.dspoclibrary.DSPOCButton
import kotlinx.coroutines.launch

class DemoComposeActivity : ComponentActivity() {

  private val activityViewModel = ActivityViewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val uiState = activityViewModel.uiState.collectAsState().value
      AndroidTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
        ) {
          POCCard(uiState = uiState) { it ->
            showToastMessage(it)
          }
        }
      }
    }
  }

  private fun showToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }
}

@Composable
fun POCCard(
  uiState: ActivityUiState,
  errorMessage: (String) -> Unit,
) {
  val coroutine = rememberCoroutineScope()
  val buttonUiState = uiState.buttonStyle
  uiState.error?.let {
    coroutine.launch {
      errorMessage(it)
    }
  }

  Surface {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top,
    ) {
      Card(
        modifier = Modifier.padding(all = 16.dp),
      ) {
        Box(modifier = Modifier.fillMaxWidth().height(170.dp)) {
          Box(modifier = Modifier.height(height = 170.dp).align(alignment = Alignment.CenterEnd)) {
            Image(
              painterResource(id = R.drawable.icon_washer),
              contentScale = ContentScale.Fit,
              contentDescription = null,
            )
          }

          val leftRightFade =
            Brush.horizontalGradient(0.38f to Color.Red, 1f to Color.Transparent)
          Box(
            modifier = Modifier.fadingEdge(leftRightFade).background(buttonUiState.backgroundColor)
              .fillMaxSize(),
          )
          Column(
            modifier = Modifier.padding(all = 24.dp),
            horizontalAlignment = Alignment.Start,
          ) {
            Text(
              text = "Connect and Wash",
              color = Color.White,
              style = MaterialTheme.typography.titleMedium.copy(
                fontSize = buttonUiState.largeFontSize,
              ),
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
              text = "Do your laundry from anywhere at your convenience",
              color = Color.White,
              style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = buttonUiState.mediumFontSize,
              ),
            )
            Spacer(modifier = Modifier.height(5.dp))
            DSPOCButton(
              buttonUiState = buttonUiState,
              title = "GET STARTED",
              onClick = {},
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(5.dp))
      Button(
        onClick = {
          uiState.loadStyle()
        },
      ) {
        Text(text = "Reload Json".uppercase())
      }
    }
  }
}

fun Modifier.fadingEdge(brush: Brush) = this
  .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
  .drawWithContent {
    drawContent()
    drawRect(brush = brush, blendMode = BlendMode.DstIn)
  }

@Preview(showBackground = true)
@Composable
fun POCCardPreview() {
  AndroidTheme {
    POCCard(uiState = ActivityUiState(loadStyle = {}, error = "Error loading style")) {}
  }
}
