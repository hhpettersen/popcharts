package no.app.popcharts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PopBarChart(
    modifier: Modifier = Modifier,
    entries: List<Float>,
    maxHeight: Dp = 200.dp,
) {
    Row {
        YAxis(
            modifier = Modifier.background(Color.LightGray),
            maxHeight = maxHeight,
        )
        Column {
            ChartView(
                modifier = modifier,
                entries = entries,
                borderColor = Color.Black,
                maxHeight = maxHeight,
            )
            XAxis(
                labels = entries.map { it.toString() },
                labelCount = 4,
            )
        }
    }
}

@Composable
private fun YAxis(
    modifier: Modifier = Modifier,
    maxHeight: Dp,
) {
    Column(
        modifier = modifier,
    ) {
        repeat(4) {
            Text(
                text = "${(100 - it * 25)}%",
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(Color.Green)
                    .height(maxHeight / 4),
            )
        }
    }
}

@Composable
private fun XAxis(
    labels: List<String>,
    labelCount: Int,
) {
    // Calculate the step to jump through the list of labels
    val step = if (labels.size > labelCount) labels.size / labelCount else 1

    // Create the X-axis labels based on the labelCount
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        labels.forEachIndexed { index, label ->
            // Display only the labels that are at positions multiple of the step value
            if (index % step == 0) {
                Text(
                    modifier = Modifier.background(Color.Red).padding(horizontal = 5.dp).weight(1f),
                    text = label,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                )
            }
        }
    }
}

@Composable
private fun ChartView(
    modifier: Modifier = Modifier,
    entries: List<Float>,
    borderColor: Color,
    maxHeight: Dp,
) {
    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }

    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(maxHeight)
                .drawBehind {
                    // draw X-Axis
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth,
                    )
                    // draw Y-Axis
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth,
                    )
                },
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        entries.forEach { item ->
            Bar(
                value = item,
                color = Color.Blue,
                maxHeight = maxHeight,
            )
        }
    }
}

@Composable
private fun RowScope.Bar(
    value: Float,
    color: Color,
    maxHeight: Dp,
) {
    val itemHeight = remember(value) { value * maxHeight.value / 100 }

    Spacer(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .height(itemHeight.dp)
            .weight(1f)
            .background(color),
    )
}

@Preview
@Composable
private fun PreviewBarChart() {
    PopBarChart(
        entries = listOf(10f, 20f, 30f, 40f, 50f, 60f, 70f, 80f),
        maxHeight = 200.dp,
    )
}
