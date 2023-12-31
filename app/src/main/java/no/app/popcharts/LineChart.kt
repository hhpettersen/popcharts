package no.app.popcharts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LineGraph(
    dataPoints: List<Float>,
    lineColor: Color = Color.Blue,
    axisLabelColor: Color = Color.Black,
    gridLineColor: Color = Color.LightGray,
    useBezier: Boolean = false,
    startFromZero: Boolean = false,
) {
    val horizontalPadding = 40.dp
    val verticalPadding = 40.dp

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        // ... Axis labels and grid lines code ...
        val yAxisLabels = listOf("Min", "Mid", "Max")
        val xAxisLabels = listOf("Start", "Mid", "End")
        var canvasWidth = 0f

        Canvas(
            modifier = Modifier.fillMaxWidth().height(200.dp)
                .padding(bottom = horizontalPadding, start = verticalPadding),
        ) {
            canvasWidth = size.width
            val max = dataPoints.maxOrNull() ?: return@Canvas
            val min = dataPoints.minOrNull() ?: return@Canvas
            val distance = max - min

            // ... Grid lines drawing code ...

            val points = dataPoints.mapIndexed { index, dataPoint ->
                val x = size.width * (index.toFloat() / (dataPoints.size - 1))
                val y = size.height * (1 - (dataPoint - min) / distance)
                Offset(x, y)
            }

            if (useBezier) {
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        val thisPoint = points[i]
                        val prevPoint = points[i - 1]
                        val controlPoint = Offset((thisPoint.x + prevPoint.x) / 2, prevPoint.y)
                        val controlPoint2 = Offset((thisPoint.x + prevPoint.x) / 2, thisPoint.y)
                        cubicTo(
                            controlPoint.x,
                            controlPoint.y,
                            controlPoint2.x,
                            controlPoint2.y,
                            thisPoint.x,
                            thisPoint.y,
                        )
                    }
                }
                drawPath(path, color = lineColor, style = Stroke(width = 5f))
            } else {
                for (i in 0 until points.size - 1) {
                    drawLine(
                        start = points[i],
                        end = points[i + 1],
                        color = lineColor,
                        strokeWidth = 5f,
                    )
                }
            }
        }

        // ... Axis labels drawing code ...
        YAxisLabels(
            yAxisLabels = yAxisLabels,
            verticalPadding = verticalPadding,
            axisLabelColor = axisLabelColor,
        )

        XAxisLabels(
            modifier = Modifier.align(Alignment.BottomStart),
            dataPoints = dataPoints,
            horizontalPadding = horizontalPadding,
            axisLabelColor = axisLabelColor,
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.YAxisLabels(
    modifier: Modifier = Modifier,
    yAxisLabels: List<String>,
    verticalPadding: Dp,
    axisLabelColor: Color,
) {
    yAxisLabels.forEachIndexed { index, label ->
        Text(
            text = label,
            color = axisLabelColor,
            modifier = modifier
                .align(Alignment.BottomStart)
                .padding(start = 4.dp, bottom = (index * 66.66).dp + verticalPadding),
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.XAxisLabels(
    modifier: Modifier = Modifier,
    dataPoints: List<Float>,
    labelCount: Int = 5, // Default to 5 labels
    axisLabelColor: Color,
    horizontalPadding: Dp,
) {
    // Determine the step size for labels
    val step = (dataPoints.size - 1) / (labelCount - 1).coerceAtLeast(1)
    val labels = (0 until labelCount).map { index ->
        // Calculate the index of the data point that corresponds to the label
        val dataIndex = (index * step).coerceIn(0, dataPoints.size - 1)
        // Format the label as needed, for example showing the value or the index
        (index + 1).toString()
    }

    Row(
        modifier = modifier
            .padding(start = horizontalPadding)
            .background(Color.Red)
            .width(with(LocalDensity.current) { maxWidth }),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        labels.forEach { label ->
            Text(
                text = label,
                color = axisLabelColor,
            )
        }
    }
}

@Preview
@Composable
fun LineGraphPreview() {
    Column {
        LineGraph(
            dataPoints = listOf(0.5f, 0.2f, 0.8f, 0.4f, 0.6f, 0.3f, 0.7f),
            useBezier = true,
        )
        LineGraph(
            dataPoints = listOf(0.5f, 0.2f, 0.8f, 0.4f, 0.6f, 0.3f, 0.7f),
            useBezier = false,
        )
//        LineGraph2(
//            dataPoints = listOf(0.5f, 0.2f, 0.8f, 0.4f, 0.6f, 0.3f, 0.7f),
//        )
    }
}
