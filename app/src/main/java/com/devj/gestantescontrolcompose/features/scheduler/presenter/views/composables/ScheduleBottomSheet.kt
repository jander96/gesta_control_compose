package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables

import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.extensions.SpacerBy
import com.devj.gestantescontrolcompose.common.ui.composables.UriImage
import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleBottomSheet(
    sheetState: SheetState,
    showBottomSheet: Boolean,
    onDismissRequest: ()->Unit,
    textMessage: String,
    onTextMessageChange: (String)->Unit,
    listOfPregnant : List<PregnantUI>,
) {


    val scope = rememberCoroutineScope()
    if(showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.padding(bottom =  48.dp),
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {

            LazyRow(){
                items(listOfPregnant){
                    UriImage(imageUri = Uri.parse(it.photo), placeholder = R.drawable.woman_avatar , size = 40.dp)
                }
            }
            Row {
                OutlinedTextField(value = textMessage, onValueChange = onTextMessageChange)
                Icon(painter = painterResource(id = R.drawable.ic_like), contentDescription = "")
            }
            SpacerBy( 64)
        }
    }
}


