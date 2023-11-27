package com.devj.gestantescontrolcompose.ui.screens.homescreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.domain.intents.HomeIntent
import com.devj.gestantescontrolcompose.ui.model.PregnantUI
import com.devj.gestantescontrolcompose.ui.screens.common.AvatarImage
import com.devj.gestantescontrolcompose.utils.convertToBitmap


@ExperimentalMaterial3Api
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    onItemClick:(pregnant: PregnantUI)-> Unit,
    onFABClick:()-> Unit,
    homeViewModel: HomeViewModel
    ) {

    val viewState by homeViewModel.viewState.collectAsStateWithLifecycle()
    val listOfPregnant = viewState.pregnantList.collectAsStateWithLifecycle(emptyList())


    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = Modifier,
        floatingActionButton = { FAB(onFABClick) }

    ) { paddingValues ->
        LaunchedEffect(key1 = homeViewModel.viewState){
            homeViewModel.intentFlow.emit(HomeIntent.EnterAtHome)
        }

        Column(modifier = modifier.padding(paddingValues)) {
            HomeHeader(
                modifier = Modifier,
                listOfPregnant.value.filter { it.isFUMReliable })
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                items(listOfPregnant.value) {
                    RecyclerItem(pregnant = it) { pregnant ->
                        onItemClick(pregnant)
                    }
                }
 
            }
        }

    }
}

@Composable
fun FAB(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    filteredPregnant: List<PregnantUI> = emptyList()
) {
    var query by remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if(filteredPregnant.isNotEmpty())Text(
            "Fum confiable",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 12.dp, top = 8.dp)
        )
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            state = LazyListState(firstVisibleItemIndex = 0, firstVisibleItemScrollOffset = 2)
        ) {
            items(filteredPregnant){ pregnant->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AvatarImage(size= 72.dp,image = if ( pregnant.photo.isNotEmpty() ) pregnant.photo.convertToBitmap() else null, placeholder = R.drawable.profile)
                    Text(pregnant.name, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        OutlinedTextField(
            label = {
                Text(
                    text = "Mis pacientes",
                )
            },
            value = query,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search bar",
                )

            },
            modifier = modifier
                .wrapContentHeight()
                .padding(8.dp),
            shape = CircleShape,
            onValueChange = { query = it }
        )
    }
}

