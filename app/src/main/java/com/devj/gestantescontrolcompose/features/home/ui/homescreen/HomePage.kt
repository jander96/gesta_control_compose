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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.domain.model.RiskClassification
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
    modifier: Modifier = Modifier,
    onItemClick: (pregnant: PregnantUI) -> Unit,
    onFABClick: () -> Unit,
    homeViewModel: HomeViewModel
) {

    val viewState by homeViewModel.viewState.collectAsState()
    val listOfPregnant = viewState.pregnantList.collectAsStateWithLifecycle(emptyList())
    val heightScreen = LocalConfiguration.current.screenHeightDp
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var showDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var pregnantId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
    

    LaunchedEffect(key1 = homeViewModel.viewState) {
        homeViewModel.sendUiEvent(HomeIntent.EnterAtHome)
    }

    LaunchedEffect(key1 = viewState.activeFilter) {
        homeViewModel.sendUiEvent(HomeIntent.OnFilterClick(viewState.activeFilter))
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
        Surface(
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .height((heightScreen * 0.35).dp)
                .align(Alignment.TopCenter),

            ) {
            HomeHeader(
                stats = Stats(
                    total = listOfPregnant.value.size,
                    onRisk = listOfPregnant.value.count { it.riskClassification == RiskClassification.HEIGHT_RISK },
                    onFinalPeriod = listOfPregnant.value.count {
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
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height((heightScreen * 0.70).dp)
                .align(Alignment.BottomCenter),
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(20.dp),
                topEnd = CornerSize(20.dp)
            )
        ) {

            LazyColumn(
                state = scrollState,
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                items(listOfPregnant.value) {
                    RecyclerItem(
                        pregnant = it,
                        onClick = { pregnant -> onItemClick(pregnant) },
                        onDelete = { pregnant ->
                            showDeleteDialog = true
                            pregnantId = pregnant.id
                        })
                }
                item { 
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }

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
            modifier = modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 56.dp, end = 23.dp),
        ) {
            FAB(onFABClick)
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

@OptIn(ExperimentalMaterial3Api::class)
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
            StatCard(value = stats.total, label = stringResource(R.string.total), cardColor = MaterialTheme.colorScheme.secondaryContainer)
            StatCard(value = stats.onRisk, label = stringResource(R.string.on_risk), cardColor = MaterialTheme.colorScheme.secondaryContainer)
            StatCard(value = stats.onFinalPeriod, label = stringResource(R.string.on_last_period), cardColor = MaterialTheme.colorScheme.secondaryContainer)

        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}

