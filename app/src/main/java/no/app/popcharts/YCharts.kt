package no.app.popcharts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

@Preview
@Composable
fun Chart() {
    val pointsData: List<Point> = List(15) {
        Point(
            x = it.toFloat(),
            y = Random.nextInt(0..100).toFloat(),
        )
    }

    println("<< ${pointsData.size}")

    val yAxisSteps = 10
    val maxRange = pointsData.maxOf { it.y }

    val xAxisData = AxisData.Builder()
        .axisStepSize(20.dp)
        .backgroundColor(Color.Blue)
        .steps(pointsData.size - 1)
        .labelData { i -> (i + 1).toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(10)
        .backgroundColor(Color.Red)
        .labelAndAxisLinePadding(20.dp)
        .labelData { index -> (index * (maxRange / yAxisSteps)).roundToInt().toString() }
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp(),
                ),
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White,
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData,
    )
}
