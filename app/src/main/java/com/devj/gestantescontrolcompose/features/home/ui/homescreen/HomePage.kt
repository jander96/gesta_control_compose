package com.devj.gestantescontrolcompose.features.home.ui.homescreen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.domain.model.RiskClassification
import com.devj.gestantescontrolcompose.common.extensions.Spacer16
import com.devj.gestantescontrolcompose.common.extensions.SpacerBy
import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI
import com.devj.gestantescontrolcompose.features.home.domain.HomeIntent
import com.devj.gestantescontrolcompose.features.home.domain.model.FilterType
import com.devj.gestantescontrolcompose.features.home.domain.model.Stats
import com.devj.gestantescontrolcompose.features.home.ui.composables.DeleteDialog
import com.devj.gestantescontrolcompose.features.home.ui.composables.RecyclerItem
import com.devj.gestantescontrolcompose.features.home.ui.composables.StatCard
import com.devj.gestantescontrolcompose.features.home.ui.viewmodel.HomeViewModel


@ExperimentalMaterial3Api
@Composable
fun HomePage(
    onItemClick: (pregnant: PregnantUI) -> Unit,
    onFABClick: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val viewState by homeViewModel.viewState.collectAsStateWithLifecycle()
    val listOfPregnant by viewState.pregnantList.collectAsStateWithLifecycle( emptyList())
    val heightScreen = LocalConfiguration.current.screenHeightDp
    val scrollState = rememberLazyListState()

    val scope = rememberCoroutineScope()
    var showDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var pregnantId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }


    LaunchedEffect(key1 = viewState.activeFilter) {
        homeViewModel.sendUiEvent(HomeIntent.OnFilterClick(viewState.activeFilter))
    }
    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                scrollState.isScrollInProgress.not() ,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                ),
                exit  = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
                ),
                modifier = Modifier
                    .padding(bottom = 56.dp, end = 23.dp),
            ) {
                FAB(onFABClick)
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(it)) {

            if(showDeleteDialog){
                DeleteDialog(
                    onAccept = {
                        showDeleteDialog = false
                        pregnantId?.let {
                            homeViewModel.sendUiEvent(HomeIntent.OnDeletePressed(it))
                        }
                    }, onDismiss = { showDeleteDialog = false }
                )
            }
            Spacer16()
            HomeHeader(
                stats = Stats(
                    total = listOfPregnant.size,
                    onRisk = listOfPregnant.count { it.riskClassification == RiskClassification.HEIGHT_RISK },
                    onFinalPeriod = listOfPregnant.count {
                        if (it.isFUMReliable) {
                            it.gestationalAgeByFUM.toFloat() >= 37.0
                        } else {
                            it.gestationalAgeByFirstUS.toFloat() >= 37.0
                        }
                    },
                ),
                onFilterClick = {filterItem->
                    homeViewModel.sendUiEvent(HomeIntent.OnFilterClick(filterItem))
                },
                onSearch = {query->
                    homeViewModel.sendUiEvent(HomeIntent.OnSearch(query))
                },
                filterType = viewState.activeFilter
            )

            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                items(listOfPregnant) {
                    RecyclerItem(
                        pregnant = it,
                        onClick = { pregnant -> onItemClick(pregnant) },
                        onDelete = { pregnant ->
                            showDeleteDialog = true
                            pregnantId = pregnant.id
                        })
                }
                item {
                    Spacer(modifier = Modifier.height(96.dp))
                }
            }

        }
    }
}

@Composable
fun FAB(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.tertiary,
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50))
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "")
    }
}
@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    stats: Stats = Stats(),
    onFilterClick: (FilterType?)->Unit,
    onSearch: (String)->Unit,
    filterType: FilterType? = null
) {
    val withScreen = LocalConfiguration.current.screenWidthDp
    var query by remember { mutableStateOf("") }
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

   var selected by rememberSaveable {
       mutableStateOf(false)
   }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(horizontalArrangement = Arrangement.SpaceAround) {
            Row {
                SpacerBy(32)
                TextField(
                    value = query,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_bar),
                        )
                    },
                    modifier = modifier
                        .width((withScreen * 0.6).dp)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.small.copy(CornerSize(50.dp)),
                    onValueChange = {
                        query = it
                        onSearch(it)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
                Box {

                    IconButton(onClick = {
                        isContextMenuVisible = true
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter_svg),
                            contentDescription = stringResource(R.string.filter),
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = isContextMenuVisible,
                        onDismissRequest = { isContextMenuVisible = false },
                        modifier = Modifier.align(Alignment.BottomCenter)

                        ) {

                            DropdownMenuItem(text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {

                                    Text(
                                        text = stringResource(R.string.higth_risk),
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.height(14.dp)
                                    )
                                    if(filterType == FilterType.onRisk)
                                        Icon(
                                            modifier = Modifier.size(12.dp),
                                            imageVector = Icons.Default.Done,
                                            contentDescription = "on risk filter" )


                                }
                            }, onClick = {
                                selected = !selected
                                if(selected){
                                    onFilterClick(FilterType.onRisk)
                                }else{
                                    onFilterClick(null)
                                }

                                isContextMenuVisible = false
                            })


                    }
                }
            }
        }


        Text(
            stringResource(R.string.stats),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 12.dp, top = 8.dp)
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            StatCard(value = stats.total, label = stringResource(R.string.total), cardColor = MaterialTheme.colorScheme.primaryContainer)
            StatCard(value = stats.onRisk, label = stringResource(R.string.on_risk), cardColor = MaterialTheme.colorScheme.primaryContainer)
            StatCard(value = stats.onFinalPeriod, label = stringResource(R.string.on_last_period), cardColor = MaterialTheme.colorScheme.primaryContainer)
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}

