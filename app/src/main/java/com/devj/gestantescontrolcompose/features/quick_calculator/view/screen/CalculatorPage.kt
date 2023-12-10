package com.devj.gestantescontrolcompose.features.quick_calculator.view.screen

import android.icu.text.SimpleDateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.extensions.Spacer16
import com.devj.gestantescontrolcompose.common.extensions.Spacer4
import com.devj.gestantescontrolcompose.common.ui.composables.Calendar
import com.devj.gestantescontrolcompose.common.ui.composables.CircularIndicator
import com.devj.gestantescontrolcompose.features.quick_calculator.domain.CalculatorIntent
import com.devj.gestantescontrolcompose.features.quick_calculator.extensions.enterFromEnd
import com.devj.gestantescontrolcompose.features.quick_calculator.extensions.enterFromStart
import com.devj.gestantescontrolcompose.features.quick_calculator.extensions.exitToEnd
import com.devj.gestantescontrolcompose.features.quick_calculator.extensions.exitToStart
import com.devj.gestantescontrolcompose.features.quick_calculator.view.composables.CustomSwitcher
import com.devj.gestantescontrolcompose.features.quick_calculator.view.viewmodel.CalculatorViewModel
import java.util.Locale

@Composable
fun CalculatorPage(
    viewModel: CalculatorViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()


    Scaffold(
        modifier = Modifier.background(Color.Red),

        ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val (graph, switcher, card, fpp) = createRefs()
            val topGuide = createGuidelineFromTop(0.05f)
            val bottomGuide = createGuidelineFromBottom(0.20f)

            CalculatorSwitcher(
                isByUs = viewState.isUsActive,
                onSwitchClick = {
                    viewModel.sendUiEvent(CalculatorIntent.SwitchClicked)
                },
                modifier = Modifier.constrainAs(switcher) {
                    top.linkTo(graph.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            DecoratedFPP(date = viewState.fpp, modifier = Modifier.constrainAs(fpp){
                bottom.linkTo(switcher.top, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            GestationalAgeGraph(
                modifier = Modifier.constrainAs(graph) {
                    top.linkTo(topGuide)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                gestationalAge = if (viewState.gestationalAge.isNotBlank())
                    viewState.gestationalAge.toFloat()
                else 0f
            )

            CalculatorCard(
                isByUs = viewState.isUsActive,
                modifier =
                Modifier
                    .padding(16.dp)
                    .constrainAs(card) {
                        top.linkTo(switcher.bottom)
                        start.linkTo(parent.start, margin = 32.dp)
                        end.linkTo(parent.end, margin = 32.dp)
                        bottom.linkTo(bottomGuide)
                    },
                fumDate = viewState.fumDate,
                onCalculateFumClick = {

                    viewModel.sendUiEvent(CalculatorIntent.CalculateButtonClicked)
                },
                onFumDateSelected = { date ->
                    viewModel.sendUiEvent(CalculatorIntent.FUMDateChanged(date))
                },
                usDate = viewState.usDate,
                onUsDateSelected = { date ->
                    viewModel.sendUiEvent(CalculatorIntent.USDateChanged(date))
                },
                onCalculateUsClick = {
                    viewModel.sendUiEvent(CalculatorIntent.CalculateButtonClicked)
                },
                onWeekSelected = { week ->

                    viewModel.sendUiEvent(CalculatorIntent.WeeksDateChanged(week))
                },
                onDaySelected = { days ->
                    viewModel.sendUiEvent(CalculatorIntent.DaysDateChanged(days))
                },
                weeks = viewState.weeks,
                days = viewState.days,
                fumButtonEnabled = viewState.isValidForFUM,
                usButtonEnabled = viewState.isValidForUSG,
            )
        }
    }
}

@Composable
fun GestationalAgeGraph(
    modifier: Modifier = Modifier,
    gestationalAge: Float
) {

    CircularIndicator(
        canvasSize = 200.dp,
        modifier = modifier,
        indicatorValue = gestationalAge,
        bigTextSuffix = "s",
        maxIndicatorValue = 42f,
        backgroundIndicatorStrokeWidth = 10f,
        foregroundIndicatorStrokeWidth = 10f,
        foregroundIndicatorColors = listOf(MaterialTheme.colorScheme.tertiary),
        bigTextStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Thin),
        bigTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f)
    )
}

@Composable
fun DecoratedFPP(
    modifier: Modifier = Modifier,
    date: String,

){
    AnimatedVisibility(date.isNotBlank(), modifier = modifier){
        val formatter = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        val formattedDate = formatter.format(SimpleDateFormat("yyyy-MM-dd").parse(date))
        Row {
            Text("FPP: ")
            Text(formattedDate)

        }
    }

}

@Composable
fun CalculatorSwitcher(
    modifier: Modifier = Modifier,
    onSwitchClick: () -> Unit,
    isByUs: Boolean,
) {

    CustomSwitcher(
        onText = " USG",
        offText = "FUM",
        size = 48.dp,
        modifier = modifier,
        isOn = isByUs,
        onClick = onSwitchClick,
        animationSpec = tween(800, 100)
    )
}


@Composable
fun CalculatorCard(
    modifier: Modifier = Modifier,
    isByUs: Boolean,
    fumDate: String,
    onCalculateFumClick: () -> Unit,
    onFumDateSelected: (String) -> Unit,
    usDate: String,
    onUsDateSelected: (String) -> Unit,
    onCalculateUsClick: () -> Unit,
    onWeekSelected: (String) -> Unit,
    onDaySelected: (String) -> Unit,
    weeks: String,
    days: String,
    usButtonEnabled: Boolean,
    fumButtonEnabled: Boolean,

    ) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            !isByUs,
            modifier = modifier,
            label = "animationCalculator",
            enter = enterFromEnd(),
            exit = exitToEnd(),
        ) {
            //calcular por Us
            FUM(
                modifier = modifier,
                date = fumDate,
                onFumDateSelected = { date -> onFumDateSelected(date) },
                onCalculateClick = onCalculateFumClick,
                fumButtonEnabled = fumButtonEnabled,
            )

        }
        AnimatedVisibility(
            isByUs,
            modifier = modifier,
            label = "animationBlue",
            enter = enterFromStart(),
            exit = exitToStart(),
        ) {
            USG(
                modifier = modifier,
                date = usDate,
                onUsDateSelected = { date -> onUsDateSelected(date) },
                onCalculateClick = onCalculateUsClick,
                weeks = weeks,
                days = days,
                onWeekSelected = { weeks -> onWeekSelected(weeks) },
                onDaySelected = { days -> onDaySelected(days) },
                usButtonEnabled = usButtonEnabled,
            )
        }

    }
}

@Composable
fun FUM(
    modifier: Modifier = Modifier,
    date: String,
    onCalculateClick: () -> Unit,
    onFumDateSelected: (String) -> Unit,
    fumButtonEnabled: Boolean = false,
) {
    var isCalendarVisible by rememberSaveable {
        mutableStateOf(false)
    }


    if (isCalendarVisible)
        Calendar(
            onDateSelected = {
                it?.let { onFumDateSelected(it) }
                isCalendarVisible = false
            },
            onClose = {
                isCalendarVisible = false
            }
        )

    Card {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.fum_calculator_tilte),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Spacer16()
            OutlinedTextField(
                modifier = Modifier.clickable {
                    isCalendarVisible = true
                },
                value = date,
                onValueChange = {},
                enabled = false,
                placeholder = {
                    Text(
                        stringResource(R.string.fum),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar_search_svg),
                        contentDescription = "fum",
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
            Spacer16()
            ElevatedButton(
                onClick = onCalculateClick,
                enabled = fumButtonEnabled,
            ) {
                Text(text = stringResource(R.string.calculate))
            }
        }
    }
}

@Composable
fun USG(
    modifier: Modifier = Modifier,
    date: String,
    onUsDateSelected: (String) -> Unit,
    onCalculateClick: () -> Unit,
    onWeekSelected: (String) -> Unit,
    onDaySelected: (String) -> Unit,
    weeks: String,
    days: String,
    usButtonEnabled: Boolean = false,

    ) {
    Card() {
        var isCalendarVisible by rememberSaveable {
            mutableStateOf(false)
        }

        if (isCalendarVisible)
            Calendar(
                onDateSelected = {
                    it?.let { onUsDateSelected(it) }
                    isCalendarVisible = false
                },
                onClose = {
                    isCalendarVisible = false
                }
            )

        Card {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.us_calculator_title),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
                Spacer16()
                Row {
                    OutlinedTextField(
                        modifier = Modifier
                            .clickable {
                                isCalendarVisible = true
                            }
                            .weight(2.5f)
                            .padding(end = 4.dp),
                        value = date,
                        onValueChange = {},
                        enabled = false,
                        placeholder = {
                            Text(
                                stringResource(R.string.usg),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        value = weeks,
                        onValueChange = onWeekSelected,
                        placeholder = { Text("s", style = MaterialTheme.typography.labelSmall) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),

                    )
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = days,
                        onValueChange = onDaySelected,
                        placeholder = { Text("d", style = MaterialTheme.typography.labelSmall) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )
                    )
                }

                Spacer4()
                Text(
                    stringResource(R.string.us_calculator_subtitle),
                    style = MaterialTheme.typography.bodySmall
                        .copy(),
                    textAlign = TextAlign.Center
                )

                Spacer16()
                ElevatedButton(
                    onClick = onCalculateClick,
                    enabled = usButtonEnabled,
                ) {
                    Text(text = stringResource(R.string.calculate))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalculatorPreview() {
    CalculatorPage()
}
