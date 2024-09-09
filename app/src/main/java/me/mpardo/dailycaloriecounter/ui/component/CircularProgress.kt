package me.mpardo.dailycaloriecounter.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalTextApi::class)
@Composable
fun CircularProgress(
    progress: Float,
    size: Dp = 64.dp,
    text: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
    textColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    barColor: Color = MaterialTheme.colors.primary,
    barBackgroundColor: Color = MaterialTheme.colors.background,
    stroke: Dp = 30.dp,
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = Modifier.size(size)) {
        drawCircle(
            radius = this.size.height / 2f - stroke.value / 2f,
            color = barBackgroundColor,
            style = Stroke(width = stroke.value)
        )

        val arcOriginX = stroke.value
        val arcOriginY = stroke.value

        drawArc(
            color = barColor,
            topLeft = Offset(arcOriginX, arcOriginY) / 2f,
            size = Size(this.size.width - arcOriginX, this.size.height - arcOriginY),
            startAngle = -90f,
            sweepAngle = progress * 360f,
            useCenter = false,
            style = Stroke(width = stroke.value),
        )

        text?.let {
            val textLayoutResult = textMeasurer.measure(
                text = AnnotatedString(it),
                maxLines = 2,
                style = textStyle,
            )

            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    (this.size.width - textLayoutResult.size.width) / 2f,
                    (this.size.height - textLayoutResult.size.height) / 2f
                ),
                color = textColor,
            )
        }
    }
}