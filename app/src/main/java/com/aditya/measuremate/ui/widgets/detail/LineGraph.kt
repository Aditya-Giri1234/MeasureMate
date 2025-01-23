package com.aditya.measuremate.ui.widgets.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.aditya.measuremate.domain.models.dashboard.BodyPartValue
import com.example.common.extension.roundToDecimal
import java.time.LocalDate

@Composable
fun LineGraph(
    modifier: Modifier = Modifier,
    bodyPartValues: List<BodyPartValue>,
    pathAndCircleWidth: Float = 5f,
    helperLineColor: Color = MaterialTheme.colorScheme.surfaceVariant ,
    textStyle : TextStyle = MaterialTheme.typography.bodySmall
) {

    val dataPointValues = bodyPartValues.asReversed().map { it.value }
    val textMeasure = rememberTextMeasurer()

    val highestValue = dataPointValues.maxOrNull() ?: 0f
    val lowestValue = dataPointValues.minOrNull() ?: 0f
    val noOfParts = 3
    val difference = (highestValue - lowestValue) / noOfParts
    val valuesList = listOf(
        highestValue.roundToDecimal(),
        (highestValue - difference).roundToDecimal(),
        (lowestValue + difference).roundToDecimal(),
        lowestValue.roundToDecimal()
    )

    Canvas(modifier) {
        val graphWidth = size.width
        val graphHeight = size.height

        val graph80PercentHeight = graphHeight * .8f
        val graph5PercentWidth = graphHeight * 0.05f
        val graph10PercentWidth = graphWidth * .1f


        valuesList.fastForEachIndexed { index, value ->

            val xPosition = 0f
            val yPosition = (graph80PercentHeight / noOfParts) * index

            drawText(
                textMeasurer = textMeasure,
                text = "$value",
                topLeft = Offset(
                    x = xPosition,
                    y = yPosition
                ) ,
                style = textStyle
            )
            drawLine(
                color = helperLineColor,
                strokeWidth = pathAndCircleWidth,
                start = Offset(
                    x = xPosition + graph10PercentWidth,
                    y = yPosition + graph5PercentWidth
                ),
                end = Offset(x = graphWidth, y = yPosition + graph5PercentWidth)
            )
        }
    }


}

@Preview
@Composable
private fun LineGraphPreview() {
    val dummyBodyPartValues = listOf(
        BodyPartValue(value = 72.0f, date = LocalDate.of(2023, 5, 10)),
        BodyPartValue(value = 76.84865145f, date = LocalDate.of(2023, 5, 1)),
        BodyPartValue(value = 74.0f, date = LocalDate.of(2023, 4, 20)),
        BodyPartValue(value = 75.1f, date = LocalDate.of(2023, 4, 5)),
        BodyPartValue(value = 66.3f, date = LocalDate.of(2023, 3, 15)),
        BodyPartValue(value = 67.2f, date = LocalDate.of(2023, 3, 10)),
        BodyPartValue(value = 73.5f, date = LocalDate.of(2023, 3, 1)),
        BodyPartValue(value = 69.8f, date = LocalDate.of(2023, 2, 18)),
        BodyPartValue(value = 68.4f, date = LocalDate.of(2023, 2, 1)),
        BodyPartValue(value = 72.0f, date = LocalDate.of(2023, 1, 22)),
        BodyPartValue(value = 70.5f, date = LocalDate.of(2023, 1, 14))
    )
    LineGraph(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 2 / 1f)
            .padding(16.dp)
            .background(Color.White), bodyPartValues = dummyBodyPartValues
    )
}