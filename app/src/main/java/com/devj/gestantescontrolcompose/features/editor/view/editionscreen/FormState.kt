package com.devj.gestantescontrolcompose.features.editor.view.editionscreen

import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import com.devj.gestantescontrolcompose.common.domain.model.DataDate
import com.devj.gestantescontrolcompose.common.domain.model.Measures
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI


class FormState(

) {

    val id = mutableIntStateOf(0)
    private val _name =  mutableStateOf("")
    val name get() = _name.value
    val nameErrorMessage: MutableState<String?> =  mutableStateOf(null)

    private val _lastName =  mutableStateOf("")
    val lastName get() = _lastName.value
    val lastNameErrorMessage: MutableState<String?> =  mutableStateOf(null)

    private val _age =  mutableStateOf("")
    val age get()= _age.value
    val ageErrorMessage: MutableState<String?> =  mutableStateOf(null)

    private val _size = mutableStateOf("")
    val size get() = _size.value
    val sizeErrorMessage: MutableState<String?> =  mutableStateOf(null)

    private val _weight = mutableStateOf("")
    val weight get() = _weight.value
    val weightErrorMessage: MutableState<String?> =  mutableStateOf(null)

    private val _phone =  mutableStateOf("")
    val phone get() = _phone.value
    val phoneErrorMessage: MutableState<String?> =  mutableStateOf(null)

    private val _firstUS =  mutableStateOf("")
    val firstUS get()= _firstUS.value

    private val _firstUsWeeks =  mutableStateOf("")
    val firstUsWeeks get()= _firstUsWeeks.value
    val weeksErrorMessage: MutableState<String?> =  mutableStateOf(null)

    private val _firstUSDays =  mutableStateOf("")
    val firstUSDays get()= _firstUSDays.value
    val daysErrorMessage: MutableState<String?> =  mutableStateOf(null)

    private val _fum =  mutableStateOf("")
    val fum get()= _fum.value

    private val _photo =  mutableStateOf("")
    val photo get()= _photo.value

    private val _isFumReliable: MutableState<Boolean> = mutableStateOf(false)
    val isFumReliable get() = _isFumReliable.value




    constructor(pregnantUI: PregnantUI?): this() {
        pregnantUI?.let { pregnant->
            id.intValue = pregnant.id
            _name.value = pregnant.name
            _lastName.value = pregnant.lastName
            _age.value = pregnant.age
            _size.value = pregnant.size
            _weight.value = pregnant.weight
            _phone.value = pregnant.phoneNumber
            _firstUS.value = pregnant.firstUS
            _fum.value = pregnant.fum
            _isFumReliable.value = pregnant.isFUMReliable
            _photo.value = pregnant.photo

           _firstUsWeeks.value =  pregnant.firstUSWeeks
           _firstUSDays.value =  pregnant.firstUSDays


        }
    }

    fun buildPregnant(): Pregnant {
       return  Pregnant(
            id = id.intValue,
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

        )
    }

    fun isValid(): Boolean {
        val errors = listOf(
            nameErrorMessage.value,
            lastNameErrorMessage.value,
            ageErrorMessage.value,
            sizeErrorMessage.value,
            weightErrorMessage.value,
            phoneErrorMessage.value,
            weeksErrorMessage.value,
            daysErrorMessage.value
        )

        val requiredFields = listOf(
            name,
            lastName,
            age,
            size,
            weight,
        )
        val hasDate = (fum.isNotEmpty() || firstUS.isNotEmpty())

        return errors.all { message -> message == null } &&
                requiredFields.none { it.isBlank() } &&
                hasDate
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

    private fun validateName(name: String):String?{
        if(name.isBlank()) return "requerido"
        return null
    }

    private fun validateLastname(lastname: String): String?{
        if(lastname.isBlank()) return "requerido"
        return null
    }

    private fun validateAge(age: String): String?{
        if(age.isBlank()) return null
        if(!age.isDigitsOnly()) return "inválido"
        val ageDigit = age.toInt()
        if(ageDigit !in 5..60) return "edad infértil"
        return null
    }

    private fun validateWeight(weight: String): String?{
        val regex = "^[0-9]+(\\.[0-9]+)?|[0-9]+\$".toRegex()
        if(weight.isBlank()) return "requerido"
        if(!regex.matches(weight)) return "inválido"
        return null
    }

    private fun validateFirstUsWeeks(weeks: String): String?{
        if(weeks.isBlank()) return "requerido"
        if(!weeks.isDigitsOnly()) return "inválido"
        if(weeks.toInt() !in 0..42) return "rango inválido"
        return null
    }

    private fun validateFirstUsDays(days: String): String?{
        if(days.isBlank()) return "requerido"
        if(!days.isDigitsOnly()) return "solo números"
        if(days.toInt() !in 0..6) return "inválido"
        return null
    }

    private fun validatePhone(phone: String): String?{
        val pattern = Patterns.PHONE
        val matcher = pattern.matcher(phone)
        if(!matcher.matches()) return "inválido"
        return null
    }

    private fun validateSize(size: String): String?{
        val regex = "^[0-9]+(\\.[0-9]+)?|[0-9]+\$".toRegex()
        if(size.isBlank()) return "requerido"
        if(!regex.matches(size)) return "inválido"
        return null
    }
    fun changePhone(phone: String) {
        this._phone.value = phone
        this.phoneErrorMessage.value = validatePhone(phone)
    }
    fun changeName(name : String){
        this._name.value = name
        this.nameErrorMessage.value = validateName(name)
    }
    fun changeLastname (lastname: String){
        this._lastName.value = lastname
        this.lastNameErrorMessage.value = validateLastname(lastname)
    }
    fun changeAge(age : String){
        this._age.value = age
        this.ageErrorMessage.value = validateAge(age)
    }
    fun changeSize(size : String){
        this._size.value = size
        this.sizeErrorMessage.value = validateSize(size)
    }
    fun changeWeight(weight: String){
        this._weight.value = weight
        this.weightErrorMessage.value = validateWeight(weight)
    }
    fun changeUsDate(us : String){
        _firstUS.value = us
    }
    fun changeUsWeeks(weeks : String){
        _firstUsWeeks.value = weeks
        weeksErrorMessage.value = validateFirstUsWeeks(weeks)
    }
    fun changeUsDays(days : String){
        _firstUSDays.value = days
        daysErrorMessage.value = validateFirstUsDays(days)
    }
    fun changeFumDate(fum : String){
        this._fum.value = fum
    }

    fun onCheckboxChange(isChecked: Boolean){
        _isFumReliable.value = isChecked
    }

    fun changePhoto(photo: String){
        _photo.value = photo
    }
}