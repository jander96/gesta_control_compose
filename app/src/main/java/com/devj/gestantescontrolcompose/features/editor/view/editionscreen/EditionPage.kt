@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.devj.gestantescontrolcompose.features.editor.view.editionscreen


import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.domain.model.RiskClassification
import com.devj.gestantescontrolcompose.common.extensions.Spacer16
import com.devj.gestantescontrolcompose.common.extensions.getBitmap
import com.devj.gestantescontrolcompose.common.extensions.timeStamp
import com.devj.gestantescontrolcompose.common.service.ContactManager
import com.devj.gestantescontrolcompose.common.ui.composables.AvatarImage
import com.devj.gestantescontrolcompose.common.ui.composables.ExpandableSection
import com.devj.gestantescontrolcompose.common.ui.composables.ImageSelectorRow
import com.devj.gestantescontrolcompose.common.ui.composables.PregnantDateSelector
import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI
import com.devj.gestantescontrolcompose.features.editor.domain.EditionIntent
import com.devj.gestantescontrolcompose.features.editor.view.composables.RadioButtonsGroup
import com.devj.gestantescontrolcompose.features.editor.view.composables.RadioOption
import com.devj.gestantescontrolcompose.features.editor.view.viewmodel.EditionViewModel
import java.io.File
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditionPage(
    pregnantId: Int?,
    editionViewModel: EditionViewModel = hiltViewModel(),
    onSaveTap: () -> Unit,
) {
    val context = LocalContext.current
    val contactManager = ContactManager(context)
    val state by editionViewModel.viewState.collectAsStateWithLifecycle()
    var pregnant: PregnantUI? by remember { mutableStateOf(null) }
    pregnant = state.pregnant
    val formState = FormState(context,pregnant)
    var photo : Bitmap? by remember{  mutableStateOf(null) }


    //Temporary
    val fileName = Calendar.getInstance().timeStamp(".jpg")
    val photoFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),fileName )
    val photoUri = FileProvider.getUriForFile(context, AUTHORITY, photoFile)
    val scope = rememberCoroutineScope()

    SideEffect {
        formState.photo?.let {
            if(it.isNotBlank()){ photo = context.getBitmap(Uri.parse(it)) }
        }
    }

    val camLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {success ->
            if(success){
                photo = context.getBitmap(photoUri)
                formState.updatePhotoUri(photoUri)
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri ->
                    val newUri = formState.createImageCopy(uri)
                    photo = context.getBitmap(newUri)
                    formState.updatePhotoUri(newUri)
            }
        }

    val readMediaStorePermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                galleryLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }

    val contactLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickContact()) { uri ->
            uri?.let {
                val numberPhone = contactManager.getContactFromUri(it)
                formState.changePhone(numberPhone ?: "")
            }
        }
    val contactPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                contactLauncher.launch()
            }
        }

    LaunchedEffect(pregnantId) {
        if (pregnantId != null && pregnantId != 0) {
            editionViewModel.sendUiEvent(EditionIntent.LoadPregnant(pregnantId))
        }
    }

    LaunchedEffect(key1 = state.isThereNewPregnant) {
        if (state.isThereNewPregnant) onSaveTap()
    }

    val error = state.error?.getContentIfNotHandled()
    error?.let {
        Toast.makeText(LocalContext.current, it.message, Toast.LENGTH_LONG).show()
    }

    Scaffold(modifier = Modifier.padding(horizontal = 16.dp)) { paddingValues ->
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
                    modifier = Modifier.padding(16.dp),
                    image = photo,
                    onCameraClick = {


                        camLauncher.launch(photoUri)
                    },
                    onGalleryClick = {
                        readMediaStorePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                    },
                    isLoading = formState.isLoading,
                )

                FormularyEditor(
                    formState = formState,
                    onContactClick = {
                        contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
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
                        editionViewModel.sendUiEvent(
                            EditionIntent.SaveDataPregnant(
                                formState.buildPregnant()
                            )
                        )

                    } else {
                        Toast.makeText(context, "Formulario inválido", Toast.LENGTH_LONG).show()
                    }


                }) {
                Text(stringResource(R.string.save))
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
    isLoading: Boolean,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Box {
            AnimatedContent(
                targetState = image,
                label = "image_change_animation",
            ) {
                AvatarImage(image = it, placeholder = R.drawable.woman_avatar)
            }

            ImageSelectorRow(
                onGalleryClick = onGalleryClick,
                onCameraClick = onCameraClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            if(isLoading) CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                strokeWidth = 3.dp
            )
        }
    }
}


@ExperimentalMaterial3Api
@Composable
fun FormularyEditor(
    modifier: Modifier = Modifier,
    formState: FormState,
    onContactClick: ()->Unit,
) {
    Column {

        ExpandableSection(
            modifier = Modifier.fillMaxWidth(),
            leading = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit_svg),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp),
                )
            },
            content = {
                Column() {

                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        OutlinedTextField(
                            textStyle = MaterialTheme.typography.labelMedium,
                            value = formState.name,
                            modifier = modifier
                                .weight(1.5f)
                                .fillMaxSize()
                                .padding(4.dp),
                            onValueChange = { formState.changeName(it) },
                            label = {
                                Text(stringResource(R.string.name), style = MaterialTheme.typography.labelMedium)
                            },
                            isError = formState.nameErrorMessage != null,
                            supportingText = {
                                if (formState.nameErrorMessage != null) Text(
                                    formState.nameErrorMessage!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)

                        )
                        OutlinedTextField(
                            textStyle = MaterialTheme.typography.labelMedium,
                            value = formState.lastName,
                            modifier = modifier
                                .weight(2f)
                                .padding(4.dp),
                            onValueChange = { formState.changeLastname(it) },
                            label = {
                                Text(stringResource(R.string.lastname), style = MaterialTheme.typography.labelMedium)
                            },
                            isError = formState.lastNameErrorMessage != null,
                            supportingText = {
                                if (formState.lastNameErrorMessage != null) Text(
                                    formState.lastNameErrorMessage!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        OutlinedTextField(
                            textStyle = MaterialTheme.typography.labelMedium,
                            value = formState.age,
                            modifier = modifier
                                .weight(1f)
                                .padding(4.dp),
                            onValueChange = { formState.changeAge(it) },
                            label = {
                                Text(stringResource(R.string.age), style = MaterialTheme.typography.labelMedium)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            isError = formState.ageErrorMessage != null,
                            supportingText = {
                                if (formState.ageErrorMessage != null) Text(
                                    formState.ageErrorMessage!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            },

                        )
                        OutlinedTextField(
                            textStyle = MaterialTheme.typography.labelMedium,
                            value = formState.size,
                            modifier = modifier
                                .weight(1f)
                                .padding(4.dp),
                            onValueChange = { formState.changeSize(it) },
                            label = {
                                Text(stringResource(R.string.size), style = MaterialTheme.typography.labelMedium)
                            },
                            suffix = { Text(stringResource(R.string.cm)) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            isError = formState.sizeErrorMessage != null,
                            supportingText = {
                                if (formState.sizeErrorMessage != null) Text(
                                    formState.sizeErrorMessage!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                        OutlinedTextField(
                            textStyle = MaterialTheme.typography.labelMedium,
                            value = formState.weight,
                            modifier = modifier
                                .weight(1f)
                                .padding(4.dp),
                            onValueChange = { formState.changeWeight(it) },
                            label = {
                                Text(stringResource(R.string.weight), style = MaterialTheme.typography.labelMedium)
                            },
                            suffix = { Text(stringResource(R.string.kg)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal,   imeAction = ImeAction.Next),
                            isError = formState.weightErrorMessage != null,
                            supportingText = {
                                if (formState.weightErrorMessage != null) Text(
                                    formState.weightErrorMessage!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }

                    Row {
                        OutlinedTextField(
                            textStyle = MaterialTheme.typography.labelMedium,
                            value = formState.phone,
                            modifier = modifier
                                .weight(1f)
                                .padding(4.dp),
                            onValueChange = { formState.changePhone(it) },
                            label = {
                                Text(stringResource(R.string.phone), style = MaterialTheme.typography.labelMedium)
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_contacts_svg),
                                    contentDescription = "pick contact",
                                    modifier = modifier
                                        .padding(16.dp)
                                        .clickable { onContactClick() }
                                        .size(24.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone,  imeAction = ImeAction.Done),
                            isError = formState.phoneErrorMessage != null,
                            supportingText = {
                                if (formState.phoneErrorMessage != null) Text(
                                    formState.phoneErrorMessage!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }

                }
            },
            text = stringResource(R.string.general_data)
        )




        Spacer16()

        ExpandableSection(
            modifier = Modifier.fillMaxWidth(),
            leading = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar_search_svg),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp),
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
                    onFumDateSelected = { formState.changeFumDate(it) },
                    onUsDateSelected = { formState.changeUsDate(it) },
                    onCheckboxChange = { formState.onCheckboxChange(it) },
                    isFumReliable = formState.isFumReliable,
                    weekErrorMessage = formState.weeksErrorMessage,
                    daysErrorMessage = formState.daysErrorMessage,
                )
            },
            text = stringResource(R.string.rigth_method)
        )
        Spacer16()

        ExpandableSection(
            modifier = Modifier.fillMaxWidth(),
            leading = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_radio_button_group_svg),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp)
                )
            },
            content = {
                RadioButtonsGroup(
                    selectedIndexByDefault = formState.riskClassification.level,
                    options = setOf(
                        RadioOption(stringResource(R.string.low_risk), RiskClassification.LOW_RISK),
                        RadioOption(stringResource(R.string.higth_risk), RiskClassification.HEIGHT_RISK),
                    ),
                    onSelect = { classification ->
                        formState.changeRiskClassification(classification)
                    })
            },
            text = stringResource(R.string.risk_classification)
        )

    }
}


