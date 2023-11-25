@file:OptIn(ExperimentalMaterial3Api::class)

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
import com.devj.gestantescontrolcompose.domain.model.Formulary
import com.devj.gestantescontrolcompose.framework.ContactManager
import com.devj.gestantescontrolcompose.ui.model.PregnantUI
import com.devj.gestantescontrolcompose.ui.screens.common.AvatarImage
import com.devj.gestantescontrolcompose.ui.screens.common.ImageSelectorRow
import com.devj.gestantescontrolcompose.ui.screens.editionscreen.FormState.*
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
    val formState = rememberFormState(pregnant)
    val scope = rememberCoroutineScope()
    var image: Bitmap? by rememberSaveable { mutableStateOf(null) }

    val camLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            image = it;
            it?.let {
                formState.changeInputValue(Companion.FieldKey.PHOTO,it.convertToString())
            }
        }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){

    }


    LaunchedEffect(pregnantId ){
        if(pregnantId != null && pregnantId != 0){
            editionViewModel.intentFlow.emit(EditionIntent.GetPregnantById(pregnantId))
        }
    }

    LaunchedEffect(key1 = state.value.isThereNewPregnant ){
        if(state.value.isThereNewPregnant) onSaveTap()
    }

    val error = state.value.error?.getContentIfNotHandled()
    error?.let {
        Toast.makeText(LocalContext.current, it.message, Toast.LENGTH_LONG).show()
    }


    LazyColumn() {
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
                pregnantUI = pregnant,
                modifier = Modifier,
                onContactPick = {

                },
                formState = formState
            )
        }
        item {
            FilledTonalButton(modifier = Modifier.padding(32.dp),onClick = {
                val form = Formulary(
                    id = pregnantId ?: 0,
                    name = formState.name.value,
                    lastName = formState.lastName.value,
                    age = formState.age.value,
                    weight = formState.weight.value,
                    size = formState.size.value,
                    phoneNumber = formState.phone.value,
                    fUM = formState.fum.value,
                    isFUMReliable = formState.isFUMReliable.value,
                    firstFUG = formState.firstUS.value,
                    riskFactors = formState.riskFactors.value,
                    notes = formState.notes.value,
                    photo = formState.photo.value
                )
                scope.launch {
                    editionViewModel.intentFlow.emit(EditionIntent.ValidateFormulary(form))
                }

                scope.launch {
                    editionViewModel.intentFlow.emit(EditionIntent.SaveDataPregnant(form))
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
    pregnantUI: PregnantUI? = null,
    modifier: Modifier = Modifier,
    onContactPick: (String) -> Unit,
    formState: FormState
) {

    val context = LocalContext.current
    val showFumCalendar = formState.showFumCalendar.collectAsStateWithLifecycle()
    val showUsCalendar = formState.showUsCalendar.collectAsStateWithLifecycle()
    val isReliable = formState.isFUMReliable.collectAsStateWithLifecycle()
    val name = formState.name.collectAsStateWithLifecycle()
    val lastName = formState.lastName.collectAsStateWithLifecycle()
    val age = formState.age.collectAsStateWithLifecycle()
    val weight = formState.weight.collectAsStateWithLifecycle()
    val size = formState.size.collectAsStateWithLifecycle()
    val phone = formState.phone.collectAsStateWithLifecycle()
    val firstUS = formState.firstUS.collectAsStateWithLifecycle()
    val secondUS = formState.secondUS.collectAsStateWithLifecycle()
    val thirdUS = formState.thirdUS.collectAsStateWithLifecycle()
    val fum = formState.fum.collectAsStateWithLifecycle()
    val riskFactors = formState.riskFactors.collectAsStateWithLifecycle()
    val notes = formState.notes.collectAsStateWithLifecycle()
    val contactManager = ContactManager(context)

    val contactLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickContact()){ uri->
        uri?.let{
            val numberPhone = contactManager.getContactFromUri(it)
            formState.changeInputValue(Companion.FieldKey.PHONE, numberPhone ?: "")
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){isGranted->
        if(isGranted){
            contactLauncher.launch()
        }
    }



    Column(modifier = modifier.padding(16.dp)) {
        if (showFumCalendar.value) Calendar(formState, onDateSelected = { date ->
            date?.let{
                formState.changeInputValue(Companion.FieldKey.FUM,it)
            }
        }, onClose = {formState.setFumCalendarVisibility(false)})
        if (showUsCalendar.value) Calendar(formState, onDateSelected = { date ->
            date?.let{
                formState.changeInputValue(Companion.FieldKey.FIRST_US,it)
            }
        }, onClose = {formState.setUsCalendarVisibility(false)})

//   Nombre y apellidos
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            TextField(
                value = name.value,
                modifier = modifier
                    .weight(1f)
                    .padding(4.dp),
                onValueChange = { formState.changeInputValue(Companion.FieldKey.NAME, it) },
                label = {
                    Text("nombre")
                }
            )
            TextField(
                value = lastName.value,
                modifier = modifier
                    .weight(2f)
                    .padding(4.dp),
                onValueChange = { formState.changeInputValue(Companion.FieldKey.LAST_NAME, it) },
                label = {
                    Text("apellidos")
                }
            )
        }
//   Edad, talla , peso
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(

                value = age.value,
                modifier = modifier
                    .weight(1f)
                    .padding(4.dp),
                onValueChange = { formState.changeInputValue(Companion.FieldKey.AGE, it) },
                label = {
                    Text("edad")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            TextField(
                value = size.value,
                modifier = modifier
                    .weight(1f)
                    .padding(4.dp),
                onValueChange = { formState.changeInputValue(Companion.FieldKey.SIZE, it) },
                label = {
                    Text("talla")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )
            TextField(
                value = weight.value,
                modifier = modifier
                    .weight(1f)
                    .padding(4.dp),
                onValueChange = { formState.changeInputValue(Companion.FieldKey.WEIGHT, it) },
                label = {
                    Text("peso")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )
        }

        Row() {
            TextField(
                value = phone.value,
                modifier = modifier
                    .weight(1f)
                    .padding(4.dp),
                onValueChange = { formState.changeInputValue(Companion.FieldKey.PHONE, it) },
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
            )
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween) {

            Column(modifier = modifier.weight(1f)) {
                TextField(
                    textStyle = MaterialTheme.typography.bodyMedium,
                    enabled = false,
                    value = firstUS.value,
                    modifier = modifier
                        .padding(4.dp)
                        .clickable { },
                    onValueChange = { formState.changeInputValue(Companion.FieldKey.FIRST_US, it) },
                    placeholder = {
                        Text("1er U/S")
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar_24),
                            contentDescription = "",
                            modifier = modifier.clickable { formState.setUsCalendarVisibility(true) }
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
                    value = fum.value,
                    modifier = modifier
                        .padding(4.dp),
                    onValueChange = { formState.changeInputValue(Companion.FieldKey.FUM, it) },
                    placeholder = {
                        Text("FUM")
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar_24),
                            contentDescription = "",
                            modifier = modifier.clickable {formState.setFumCalendarVisibility(true) }
                        )
                    }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isReliable.value,
                        onCheckedChange = { formState.setReliability(it) })
                    Text("Es confiable?")
                }
            }
        }

        TextField(
            value = riskFactors.value,
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            onValueChange = { formState.changeInputValue(Companion.FieldKey.RISK_FACTORS, it) },
            label = {
                Text("factores de riesgo")
            },
            supportingText = { Text("separados por coma,") }
        )
        TextField(
            value = notes.value,
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            onValueChange = { formState.changeInputValue(Companion.FieldKey.NOTES, it) },
            label = {
                Text("notas")
            }
        )

    }
}


@Composable
fun Calendar(
    formState: FormState,
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