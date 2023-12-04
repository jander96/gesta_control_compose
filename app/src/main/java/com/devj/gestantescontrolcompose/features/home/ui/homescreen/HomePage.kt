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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.extensions.SpacerBy
import com.devj.gestantescontrolcompose.common.extensions.convertToBitmap
import com.devj.gestantescontrolcompose.common.ui.composables.AvatarImage
import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI
import com.devj.gestantescontrolcompose.features.home.domain.HomeIntent
import com.devj.gestantescontrolcompose.features.home.ui.composables.DeleteDialog
import com.devj.gestantescontrolcompose.features.home.ui.composables.RecyclerItem
import com.devj.gestantescontrolcompose.features.home.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch


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
        homeViewModel.intentFlow.emit(HomeIntent.EnterAtHome)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if(showDeleteDialog){
            DeleteDialog(
                onAccept = {
                    showDeleteDialog = false
                    pregnantId?.let {
                        scope.launch { homeViewModel.intentFlow.emit(HomeIntent.OnDeletePressed(it)) }
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
                filteredPregnant =
                if (listOfPregnant.value.size > 3)
                    listOfPregnant.value.takeLast(3)
                else listOfPregnant.value
            ){filterItem->

            }
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
    filteredPregnant: List<PregnantUI> = emptyList(),
    onFilterClick: (String)->Unit
) {
    val withScreen = LocalConfiguration.current.screenWidthDp
    var query by remember { mutableStateOf("") }
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val dropDownItems = listOf("A término", "Alto riesgo",)

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
                            contentDescription = "search bar",
                        )
                    },
                    modifier = modifier
                        .width((withScreen * 0.6).dp)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.small.copy(CornerSize(50.dp)),
                    onValueChange = { query = it },
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
                            contentDescription = "filter",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }


                    DropdownMenu(
                        expanded = isContextMenuVisible,
                        onDismissRequest = { isContextMenuVisible = false },
                        modifier = Modifier.align(Alignment.BottomCenter)

                        ) {
                        dropDownItems.forEach { item ->
                            DropdownMenuItem(text = {
                                Text(
                                    text = item,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.height(14.dp)
                                )
                            }, onClick = {
                                onFilterClick(item)
                                isContextMenuVisible = false
                            })
                        }

                    }
                }



            }

        }

        if (filteredPregnant.isNotEmpty())
            Text(
                "Recientes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(bottom = 12.dp, top = 8.dp)
            )
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
        ) {
            items(filteredPregnant) { pregnant ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    AvatarImage(
                        size = 40.dp,
                        image = if (pregnant.photo.isNotEmpty()) pregnant.photo.convertToBitmap() else null,
                        placeholder = R.drawable.profile
                    )
                    Text(pregnant.name, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}

