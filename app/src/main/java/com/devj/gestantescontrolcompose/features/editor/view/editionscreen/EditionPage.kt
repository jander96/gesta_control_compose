@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.devj.gestantescontrolcompose.features.editor.view.editionscreen


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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.devj.gestantescontrolcompose.common.extensions.Spacer16
import com.devj.gestantescontrolcompose.common.extensions.convertToBitmap
import com.devj.gestantescontrolcompose.common.extensions.convertToString
import com.devj.gestantescontrolcompose.common.service.ContactManager
import com.devj.gestantescontrolcompose.common.ui.composables.AvatarImage
import com.devj.gestantescontrolcompose.common.ui.composables.ExpandableSection
import com.devj.gestantescontrolcompose.common.ui.composables.ImageSelectorRow
import com.devj.gestantescontrolcompose.common.ui.composables.LottieAnimationLoader
import com.devj.gestantescontrolcompose.common.ui.composables.PregnantDateSelector
import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI
import com.devj.gestantescontrolcompose.features.editor.domain.EditionIntent
import com.devj.gestantescontrolcompose.features.editor.view.composables.Stepper
import com.devj.gestantescontrolcompose.features.editor.view.viewmodel.EditionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditionPage(
    pregnantId: Int?,
    editionViewModel: EditionViewModel = hiltViewModel(),
    onSaveTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state = editionViewModel.viewState.collectAsStateWithLifecycle()
    var pregnant: PregnantUI? by remember { mutableStateOf(null) }
    pregnant = state.value.pregnant
    val scope = rememberCoroutineScope()
    var image: Bitmap? by rememberSaveable { mutableStateOf(null) }
    val contactManager = ContactManager(context)
    val formState = FormState(pregnant)


    val camLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            image = it
            it?.let {
                formState.changePhoto(it.convertToString())
            }
        }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {

    }


    LaunchedEffect(pregnantId) {
        if (pregnantId != null && pregnantId != 0) {
            editionViewModel.intentFlow.emit(EditionIntent.LoadPregnant(pregnantId))
        }
    }

    LaunchedEffect(key1 = state.value.isThereNewPregnant) {
        if (state.value.isThereNewPregnant) onSaveTap()
    }

    val error = state.value.error?.getContentIfNotHandled()
    error?.let {
        Toast.makeText(LocalContext.current, it.message, Toast.LENGTH_LONG).show()
    }

    Scaffold(modifier = modifier.padding(horizontal = 16.dp)) { paddingValues ->
        val scrollState = rememberScrollState()

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {

                IconButton(onSaveTap) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }


                HeaderEditor(
                    image = if (state.value.pregnant?.photo?.isNotEmpty() == true) state.value.pregnant?.photo?.convertToBitmap() else image,
                    modifier = Modifier,
                    onCameraClick = {
                        camLauncher.launch()
                    },
                    onGalleryClick = {
                        galleryLauncher.launch("image")
                    },
                    numberOfSteps = 3,
                    currentStep = 2,

                    )


                FormularyEditor(
                    formState = formState,
                    contactManager = contactManager,
                )

                Spacer(modifier = Modifier.size(height = 56.dp, width = 0.dp))


            }
            FilledTonalButton(
                enabled = formState.isValid(),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
                onClick = {
                    formState.validateAllRequiredFields()
                    if (formState.isValid()) {
                        scope.launch {
                            editionViewModel.intentFlow.emit(
                                EditionIntent.SaveDataPregnant(
                                    formState.buildPregnant()
                                )
                            )
                        }
                    } else {
                        Toast.makeText(context, "Formulario invalido", Toast.LENGTH_LONG).show()
                    }


                }) {
                Text("Guardar")
            }

        }
    }

}


@Composable
fun HeaderEditor(
    image: Bitmap?,
    modifier: Modifier = Modifier,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    numberOfSteps: Int = 0,
    currentStep: Int = 0,
) {
    Row {
        Box(modifier = modifier.padding(16.dp)) {
            AvatarImage(image = image, placeholder = R.drawable.profile)
            ImageSelectorRow(
                onGalleryClick = onGalleryClick,
                onCameraClick = onCameraClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        Stepper(numberOfSteps = numberOfSteps, currentStep = currentStep)
    }
}


@ExperimentalMaterial3Api
@Composable
fun FormularyEditor(
    modifier: Modifier = Modifier,
    formState: FormState = FormState(),
    contactManager: ContactManager,
) {


    val contactLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickContact()) { uri ->
            uri?.let {
                val numberPhone = contactManager.getContactFromUri(it)
                formState.changePhone(numberPhone ?: "")
            }
        }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                contactLauncher.launch()
            }
        }



    Column {

        ExpandableSection(
            modifier = Modifier.fillMaxWidth(),
            leading = {
                LottieAnimationLoader(
                    rawRes = R.raw.lottie_edit,
                    modifier = Modifier.size(48.dp)
                )
            },
            content = {
                Column() {

                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        OutlinedTextField(
                            value = formState.name,
                            modifier = modifier
                                .weight(1.5f)
                                .fillMaxSize()
                                .padding(4.dp),
                            onValueChange = { formState.changeName(it) },
                            label = {
                                Text("nombre")
                            },
                            isError = formState.nameErrorMessage.value != null,
                            supportingText = {
                                if (formState.nameErrorMessage.value != null) Text(
                                    formState.nameErrorMessage.value!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                        )
                        OutlinedTextField(
                            value = formState.lastName,
                            modifier = modifier
                                .weight(2f)
                                .padding(4.dp),
                            onValueChange = { formState.changeLastname(it) },
                            label = {
                                Text("apellidos")
                            },
                            isError = formState.lastNameErrorMessage.value != null,
                            supportingText = {
                                if (formState.lastNameErrorMessage.value != null) Text(
                                    formState.lastNameErrorMessage.value!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }

                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        OutlinedTextField(
                            value = formState.age,
                            modifier = modifier
                                .weight(1f)
                                .padding(4.dp),
                            onValueChange = { formState.changeAge(it) },
                            label = {
                                Text("edad")
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = formState.ageErrorMessage.value != null,
                            supportingText = {
                                if (formState.ageErrorMessage.value != null) Text(
                                    formState.ageErrorMessage.value!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                        OutlinedTextField(
                            value = formState.size,
                            modifier = modifier
                                .weight(1f)
                                .padding(4.dp),
                            onValueChange = { formState.changeSize(it) },
                            label = {
                                Text("talla")
                            },
                            suffix = { Text("cm") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = formState.sizeErrorMessage.value != null,
                            supportingText = {
                                if (formState.sizeErrorMessage.value != null) Text(
                                    formState.sizeErrorMessage.value!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                        OutlinedTextField(
                            value = formState.weight,
                            modifier = modifier
                                .weight(1f)
                                .padding(4.dp),
                            onValueChange = { formState.changeWeight(it) },
                            label = {
                                Text("peso")
                            },
                            suffix = { Text("kg") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = formState.weightErrorMessage.value != null,
                            supportingText = {
                                if (formState.weightErrorMessage.value != null) Text(
                                    formState.weightErrorMessage.value!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }

                    Row {
                        OutlinedTextField(
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
                                    painter = painterResource(id = R.drawable.ic_contacts_svg),
                                    contentDescription = "",
                                    modifier = modifier
                                        .padding(16.dp)
                                        .clickable {
                                            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                                        }
                                        .size(24.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            isError = formState.phoneErrorMessage.value != null,
                            supportingText = {
                                if (formState.phoneErrorMessage.value != null) Text(
                                    formState.phoneErrorMessage.value!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }

                }
            },
            text = "Datos generales" )




        Spacer16()

        ExpandableSection(
            modifier = Modifier.fillMaxWidth(),
            leading = {
                LottieAnimationLoader(
                    rawRes = R.raw.lottie_calendar,
                    modifier = Modifier.size(48.dp)
                )
            },
            content = {
                PregnantDateSelector(
                    fumDate = formState.fum,
                    usDate = formState.firstUS,
                    weeks = formState.firstUsWeeks,
                    days = formState.firstUSDays,
                    onWeekChange = { formState.changeUsWeeks(it) },
                    onDaysChange = { formState.changeUsDays(it) },
                    onFumDateSelected = {formState.changeFumDate(it)},
                    onUsDateSelected = { formState.changeUsDate(it) },
                    onCheckboxChange = {formState.onCheckboxChange(it)},
                    isFumReliable = formState.isFumReliable,
                    weekErrorMessage = formState.weeksErrorMessage.value,
                    daysErrorMessage = formState.daysErrorMessage.value,
                )
            },
            text = "Escoge el seguimiento adecuado" )

    }
}


