@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.devj.gestantescontrolcompose.ui.screens.editionscreen


import android.Manifest
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.domain.intents.EditionIntent
import com.devj.gestantescontrolcompose.framework.ContactManager
import com.devj.gestantescontrolcompose.ui.model.PregnantUI
import com.devj.gestantescontrolcompose.ui.screens.common.AvatarImage
import com.devj.gestantescontrolcompose.ui.screens.common.ImageSelectorRow
import com.devj.gestantescontrolcompose.utils.convertToBitmap
import com.devj.gestantescontrolcompose.utils.convertToString
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditionPage(
    pregnantId: Int?,
    editionViewModel: EditionViewModel = hiltViewModel(),
    onSaveTap: ()->Unit,
) {
    val state = editionViewModel.viewState.collectAsStateWithLifecycle()
    var pregnant: PregnantUI? by remember { mutableStateOf(null) }
    pregnant = state.value.pregnant
    val scope = rememberCoroutineScope()
    var image: Bitmap? by rememberSaveable { mutableStateOf(null) }
    val contactManager = ContactManager(LocalContext.current)
    val formState = FormState(pregnant)


    val camLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            image = it
            it?.let {
                formState.changePhoto(it.convertToString())
            }
        }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){

    }


    LaunchedEffect(pregnantId ){
        if(pregnantId != null && pregnantId != 0){
            editionViewModel.intentFlow.emit(EditionIntent.LoadPregnant(pregnantId))
        }
    }

    LaunchedEffect(key1 = state.value.isThereNewPregnant ){
        if(state.value.isThereNewPregnant) onSaveTap()
    }

    val error = state.value.error?.getContentIfNotHandled()
    error?.let {
        Toast.makeText(LocalContext.current, it.message, Toast.LENGTH_LONG).show()
    }


    LazyColumn {
        item {
            IconButton(onSaveTap) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
        }
        item {
            HeaderEditor(
                image =  if(state.value.pregnant?.photo?.isNotEmpty() == true) state.value.pregnant?.photo?.convertToBitmap() else image,
                modifier = Modifier,
                onCameraClick = {
                    camLauncher.launch()
                },
                onGalleryClick = {
                    galleryLauncher.launch("image")
                })
        }
        item {
            FormularyEditor(
                formState = formState,
                contactManager = contactManager,
            )
        }
        item {
            FilledTonalButton(modifier = Modifier.padding(32.dp),onClick = {
                scope.launch {
                    editionViewModel.intentFlow.emit(EditionIntent.SaveDataPregnant(formState.getPregnant()))
                }

            }) {
                Text("Guardar", modifier = Modifier.fillMaxWidth())
            }
        }


    }

}


@Composable
fun HeaderEditor(
    image: Bitmap?,
    modifier: Modifier = Modifier,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    Box(modifier = modifier.padding(16.dp)) {
        AvatarImage(image = image, placeholder = R.drawable.profile)
        ImageSelectorRow(
            onGalleryClick = onGalleryClick,
            onCameraClick = onCameraClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@ExperimentalMaterial3Api
@Composable
fun FormularyEditor(
    modifier: Modifier = Modifier,

    formState: FormState = FormState(),
    contactManager : ContactManager,
) {


    val contactLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickContact()){ uri->
        uri?.let{
            val numberPhone = contactManager.getContactFromUri(it)
            formState.changePhone(numberPhone ?: "")
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){isGranted->
        if(isGranted){
            contactLauncher.launch()
        }
    }



    Column(modifier = modifier.padding(16.dp)) {
        if (formState.showFumCalendar) Calendar(
            onDateSelected = { it?.let{formState.changeFumDate(it)} },
            onClose = { formState.changeFumCalendarVisibility(false) }
        )
        if (formState.showUsCalendar) Calendar(
            onDateSelected = { it?.let{formState.changeUsDate(it)} },
            onClose = { formState.changeUsCalendarVisibility(false) })

//   Nombre y apellidos
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            TextField(
                value = formState.name,
                modifier = modifier
                    .weight(1.5f)
                    .padding(4.dp),
                onValueChange = { formState.changeName(it) },
                label = {
                    Text("nombre")
                },
                isError = formState.nameErrorMessage.value != null

            )
            TextField(
                value = formState.lastName,
                modifier = modifier
                    .weight(2f)
                    .padding(4.dp),
                onValueChange = { formState.changeLastname(it) },
                label = {
                    Text("apellidos")
                },
                isError = formState.lastNameErrorMessage.value != null
            )
        }
//   Edad, talla , peso
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(
                value = formState.age,
                modifier = modifier
                    .weight(1f)
                    .padding(4.dp),
                onValueChange = { formState.changeAge(it) },
                label = {
                    Text("edad")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = formState.ageErrorMessage.value != null
            )
            TextField(
                value = formState.size,
                modifier = modifier
                    .weight(1f)
                    .padding(4.dp),
                onValueChange = {formState.changeSize(it) },
                label = {
                    Text("talla")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = formState.sizeErrorMessage.value != null
            )
            TextField(
                value = formState.weight,
                modifier = modifier
                    .weight(1f)
                    .padding(4.dp),
                onValueChange ={formState.changeWeight(it)},
                label = {
                    Text("peso")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = formState.weightErrorMessage.value != null
            )
        }

        Row{
            TextField(
                value = formState.phone,
                modifier = modifier
                    .weight(1f)
                    .padding(4.dp),
                onValueChange = { formState.changePhone(it) },
                label = {
                    Text("telefono")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person_search_24),
                        contentDescription = "",
                        modifier = modifier.clickable {
                            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        }
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = formState.phoneErrorMessage.value != null
            )
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween) {

            Column(modifier = modifier.weight(1f)) {
                TextField(
                    textStyle = MaterialTheme.typography.bodyMedium,
                    enabled = false,
                    value = formState.firstUS,
                    modifier = modifier
                        .padding(4.dp)
                        .clickable { },
                    onValueChange = {formState.changeUsDate(it)},
                    placeholder = {
                        Text("1er U/S")
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar_24),
                            contentDescription = "",
                            modifier = modifier.clickable { formState.changeUsCalendarVisibility(true) }
                        )
                    },

                )

            }
            Column(
                modifier = modifier
                    .weight(1f)
            ) {
                TextField(
                    enabled = false,
                    value = formState.fum,
                    modifier = modifier
                        .padding(4.dp),
                    onValueChange = { formState.changeFumDate(it) },
                    placeholder = {
                        Text("FUM")
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar_24),
                            contentDescription = "",
                            modifier = modifier.clickable {formState.changeFumCalendarVisibility(true) }
                        )
                    }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = formState.isFumReliable,
                        onCheckedChange = { formState.onCheckboxChange(it) })
                    Text("¿Es confiable?")
                }
            }
        }

        TextField(
            value = formState.riskFactors,
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            onValueChange = {formState.changeRiskFactors(it)},
            label = {
                Text("factores de riesgo")
            },
            supportingText = { Text("separados por coma,") },
            isError = formState.riskFactorsErrorMessage.value != null
        )
        TextField(
            value = formState.notes,
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            onValueChange = { formState.changeNote(it) },
            label = {
                Text("notas")
            },
            isError = formState.notesErrorMessage.value != null
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    onDateSelected :(String?)-> Unit,
    onClose :()-> Unit,
    ) {
    val pickerState = rememberDatePickerState()
//    DatePickerDialog
    //DatePicker

    DatePickerDialog(
        modifier = Modifier.padding(16.dp),
        onDismissRequest = onClose,

        confirmButton = {
            ElevatedButton(onClick = {
                val millis = pickerState.selectedDateMillis
                val date =  millis?.let {
                    val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                    "${localDate.dayOfMonth}/${localDate.monthValue}/${localDate.year}"
                }
                onDateSelected(date)
                onClose()
            }) {
                Text("Confirmar")
            }


        }) {
        DatePicker(
            state = pickerState
        )
    }

}