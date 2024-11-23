package com.devj.gestantescontrolcompose.features.home.ui.homescreen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.domain.model.RiskClassification
import com.devj.gestantescontrolcompose.common.extensions.Spacer16
import com.devj.gestantescontrolcompose.common.extensions.Spacer4
import com.devj.gestantescontrolcompose.common.extensions.Spacer8
import com.devj.gestantescontrolcompose.common.extensions.SpacerBy
import com.devj.gestantescontrolcompose.common.presenter.composables.InfiniteLottieAnimation
import com.devj.gestantescontrolcompose.common.presenter.model.PregnantUI
import com.devj.gestantescontrolcompose.features.home.domain.HomeIntent
import com.devj.gestantescontrolcompose.features.home.domain.model.Stats
import com.devj.gestantescontrolcompose.features.home.ui.composables.DeleteDialog
import com.devj.gestantescontrolcompose.features.home.ui.composables.HomeHeader
import com.devj.gestantescontrolcompose.features.home.ui.composables.RecyclerItem
import com.devj.gestantescontrolcompose.features.home.ui.viewmodel.HomeViewModel


@ExperimentalMaterial3Api
@Composable
fun HomePage(
    onItemClick: (pregnant: PregnantUI) -> Unit,
    onFABClick: () -> Unit,
    homeViewModel: HomeViewModel,
) {

    val viewState by homeViewModel.viewState.collectAsStateWithLifecycle()
    val listOfPregnant by viewState.pregnantList.collectAsStateWithLifecycle( emptyList())
    val scrollState = rememberLazyListState()
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var pregnantId by rememberSaveable { mutableStateOf<Int?>(null) }
    val total = viewState.total.collectAsState(initial = 0).value
    val onRisk = viewState.onRisk.collectAsState(initial = 0).value
    val onFinalPeriod = viewState.onFinalPeriod.collectAsState(initial = 0).value
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
            ) {
                FAB(onFABClick)
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

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
                    total = total,
                    onRisk = onRisk,
                    onFinalPeriod = onFinalPeriod,
                ),
                onSearch = {query->
                    homeViewModel.sendUiEvent(HomeIntent.OnSearch(query))
                },
            )
            AnimatedVisibility(listOfPregnant.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    InfiniteLottieAnimation(rawRes = R.raw.empty_list, modifier = Modifier.size(180.dp), contentScale = ContentScale.Crop)
                    Spacer8()
                    Text(text = stringResource(R.string.no_pregnant), style = MaterialTheme.typography.labelLarge)
                    Spacer4()
                    Text(text = stringResource(R.string.add_pregnant), style = MaterialTheme.typography.labelMedium)
                    SpacerBy(value = 80)
                }
            }
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
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50))
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "")
    }
}


