package com.devj.gestantescontrolcompose.features.editor.view.editionscreen

import android.content.Context
import android.net.Uri
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.domain.model.DataDate
import com.devj.gestantescontrolcompose.common.domain.model.Measures
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import com.devj.gestantescontrolcompose.common.domain.model.RiskClassification
import com.devj.gestantescontrolcompose.common.extensions.createInternalFileFromImageUri
import com.devj.gestantescontrolcompose.common.extensions.timeStamp
import com.devj.gestantescontrolcompose.common.presenter.model.PregnantUI
import java.io.File
import java.util.Calendar

const val AUTHORITY = "com.devj.gestantescontrolcompose.fileprovider"
class FormState ( private val context: Context) {

    var id by mutableIntStateOf(0)
    private var _name by  mutableStateOf("")
    val name get() = _name
    var nameErrorMessage by  mutableStateOf<String?>(null)

    private var _lastName by  mutableStateOf("")
    val lastName get() = _lastName
    var lastNameErrorMessage by  mutableStateOf<String?>(null)

    private var _age by  mutableStateOf("")
    val age get()= _age
    var ageErrorMessage  by  mutableStateOf<String?>(null)

    private var _size by mutableStateOf("")
    val size get() = _size
    var sizeErrorMessage  by  mutableStateOf<String?>(null)

    private var _weight by mutableStateOf("")
    val weight get() = _weight
    var weightErrorMessage  by  mutableStateOf<String?>(null)

    private var _phone by  mutableStateOf("")
    val phone get() = _phone
    var phoneErrorMessage  by  mutableStateOf<String?>(null)

    private var _firstUS by  mutableStateOf("")
    val firstUS get()= _firstUS

    private var _firstUsWeeks by  mutableStateOf("")
    val firstUsWeeks get()= _firstUsWeeks
    var weeksErrorMessage  by  mutableStateOf<String?>(null)

    private var _firstUSDays by  mutableStateOf("")
    val firstUSDays get()= _firstUSDays
    var daysErrorMessage  by  mutableStateOf<String?>(null)

    private var _fum by  mutableStateOf("")
    val fum get()= _fum

    private var _photo by  mutableStateOf<String?>(null)
    val photo get()= _photo

    private var _isFumReliable by mutableStateOf<Boolean>(false)
    val isFumReliable get() = _isFumReliable

    private var _riskClassification by mutableStateOf(RiskClassification.LOW_RISK)
    val riskClassification get() = _riskClassification

    private var _isLoading by mutableStateOf(false)
    val isLoading get() = _isLoading



    constructor(context: Context,pregnantUI: PregnantUI?): this(context) {
        pregnantUI?.let { pregnant->
            id = pregnant.id
            _name = pregnant.name
            _lastName = pregnant.lastName
            _age = pregnant.age
            _size = pregnant.size
            _weight = pregnant.weight
            _phone = pregnant.phoneNumber
            _firstUS = pregnant.firstUS
            _fum = pregnant.fum
            _isFumReliable = pregnant.isFUMReliable
            _photo = pregnant.photo
            _riskClassification = pregnant.riskClassification
           _firstUsWeeks =  pregnant.firstUSWeeks
           _firstUSDays =  pregnant.firstUSDays

        }
    }


    fun buildPregnant(): Pregnant {
       return  Pregnant(
            id = id,
            name = name,
            lastName = lastName,
            age = age.toInt(),
            phoneNumber = phone,
            Measures(weight.toDouble(), size.toDouble()),
            dataDate = DataDate(
                fum,
                isFumReliable,
                firstUS,
                firstUSWeeks = if(firstUsWeeks.isNotEmpty()) firstUsWeeks.toInt() else null,
                firstUSDays = if(firstUSDays.isNotEmpty()) firstUSDays.toInt() else null
            ),
            photo = photo,
           riskClassification = _riskClassification

        )
    }

    fun isValid(): Boolean {
        val errors = listOf(
            nameErrorMessage,
            lastNameErrorMessage,
            ageErrorMessage,
            sizeErrorMessage,
            weightErrorMessage,
            phoneErrorMessage,
            weeksErrorMessage,
            daysErrorMessage,
        )

        val requiredFields = listOf(
            name,
            lastName,
            age,
            size,
            weight,
        )
        val hasDate = (
                fum.isNotEmpty() || (
                        firstUS.isNotEmpty() &&
                                firstUSDays.isNotBlank() &&
                                firstUsWeeks.isNotBlank()
                        )
                )

        val isInsecureCalculateGestationalAge =
            (fum.isNotBlank() && !isFumReliable) &&
                    (firstUS.isBlank() || firstUSDays.isBlank() || firstUsWeeks.isBlank())

        return errors.all { message -> message == null } &&
                requiredFields.none { it.isBlank() } &&
                hasDate &&
                !isInsecureCalculateGestationalAge
    }
    fun validateAllRequiredFields(){
        validateName(name)
        validateLastname(lastName)
        validateAge(age)
        validateSize(size)
        validateWeight(weight)

        if(firstUS.isNotEmpty()){
            validateFirstUsDays(firstUSDays)
            validateFirstUsWeeks(firstUsWeeks)
        }
    }

    private fun toggleLoadingState(){
        _isLoading = !_isLoading
    }


    private fun validateName(name: String):String?{
        if(name.isBlank()) return context.getString(R.string.required)
        return null
    }

    private fun validateLastname(lastname: String): String?{
        if(lastname.isBlank()) return context.getString(R.string.required)
        return null
    }

    private fun validateAge(age: String): String?{
        if(age.isBlank()) return null
        if(!age.isDigitsOnly()) return context.getString(R.string.invalid)
        val ageDigit = age.toInt()
        if(ageDigit !in 5..60) return  context.getString(R.string.invalid)
        return null
    }

    private fun validateWeight(weight: String): String?{
        val regex = "^[0-9]+(\\.[0-9]+)?|[0-9]+\$".toRegex()
        if(weight.isBlank()) return context.getString(R.string.required)
        if(!regex.matches(weight)) return  context.getString(R.string.invalid)
        return null
    }

    private fun validateFirstUsWeeks(weeks: String): String?{
        if(weeks.isBlank()) return context.getString(R.string.required)
        if(!weeks.isDigitsOnly()) return context.getString(R.string.invalid)
        if(weeks.toInt() !in 0..42) return context.getString(R.string.invalid)
        return null
    }

    private fun validateFirstUsDays(days: String): String?{
        if(days.isBlank()) return context.getString(R.string.required)
        if(!days.isDigitsOnly()) return context.getString(R.string.only_numbers)
        if(days.toInt() !in 0..6) return context.getString(R.string.invalid)
        return null
    }

    private fun validatePhone(phone: String): String?{
        val pattern = Patterns.PHONE
        val matcher = pattern.matcher(phone)
        if(!matcher.matches()) return context.getString(R.string.invalid)
        return null
    }

    private fun validateSize(size: String): String?{
        val regex = "^[0-9]+(\\.[0-9]+)?|[0-9]+\$".toRegex()
        if(size.isBlank()) return context.getString(R.string.required)
        if(!regex.matches(size)) return context.getString(R.string.invalid)
        return null
    }
    fun changePhone(phone: String) {
        this._phone = phone
        this.phoneErrorMessage = validatePhone(phone)
    }
    fun changeName(name : String){
        this._name = name
        this.nameErrorMessage = validateName(name)
    }
    fun changeLastname (lastname: String){
        this._lastName = lastname
        this.lastNameErrorMessage = validateLastname(lastname)
    }
    fun changeAge(age : String){
        this._age = age
        this.ageErrorMessage = validateAge(age)
    }
    fun changeSize(size : String){
        this._size = size
        this.sizeErrorMessage = validateSize(size)
    }
    fun changeWeight(weight: String){
        this._weight = weight
        this.weightErrorMessage = validateWeight(weight)
    }
    fun changeUsDate(us : String){
        _firstUS = us
    }
    fun changeUsWeeks(weeks : String){
        _firstUsWeeks = weeks
        weeksErrorMessage = validateFirstUsWeeks(weeks)
    }
    fun changeUsDays(days : String){
        _firstUSDays = days
        daysErrorMessage = validateFirstUsDays(days)
    }
    fun changeFumDate(fum : String){
        this._fum = fum
    }

    fun onCheckboxChange(isChecked: Boolean){
        _isFumReliable = isChecked
    }


    fun updatePhotoUri(uri: Uri){
        _photo = uri.toString()
    }

    suspend fun createImageCopy(originUri: Uri): Uri{
        _isLoading = true
        val name = Calendar.getInstance().timeStamp(".jpg")
        context.createInternalFileFromImageUri(originUri, name)
        _isLoading = false
        val file = File(context.filesDir,name )
        return FileProvider.getUriForFile(context, AUTHORITY, file)
    }

    fun changeRiskClassification(classification: RiskClassification){
        _riskClassification = classification
    }
}