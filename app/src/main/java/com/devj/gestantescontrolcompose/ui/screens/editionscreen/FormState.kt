package com.devj.gestantescontrolcompose.ui.screens.editionscreen

import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import com.devj.gestantescontrolcompose.domain.model.DataDate
import com.devj.gestantescontrolcompose.domain.model.Measures
import com.devj.gestantescontrolcompose.domain.model.Pregnant
import com.devj.gestantescontrolcompose.domain.model.RiskFactor
import com.devj.gestantescontrolcompose.ui.model.PregnantUI

///Simple class state container for pregnant form
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

    private val _notes =  mutableStateOf("")
    val notes get()= _notes.value
    val notesErrorMessage: MutableState<String?> =  mutableStateOf(null)

    private val _riskFactors =  mutableStateOf("")
    val riskFactors get()= _riskFactors.value
    val riskFactorsErrorMessage: MutableState<String?> =  mutableStateOf(null)

    private val _firstUS =  mutableStateOf("")
    val firstUS get()= _firstUS.value

    private val _fum =  mutableStateOf("")
    val fum get()= _fum.value

    private val _photo =  mutableStateOf("")
    val photo get()= _photo.value

    private val _isFumReliable: MutableState<Boolean> = mutableStateOf(false)
    val isFumReliable get() = _isFumReliable.value

    private val _showFumCalendar: MutableState<Boolean> = mutableStateOf(false)
    val showFumCalendar get()= _showFumCalendar.value

    private val _showUsCalendar : MutableState<Boolean> = mutableStateOf(false)
    val showUsCalendar get() = _showUsCalendar.value



    constructor(pregnant: PregnantUI?): this() {
        pregnant?.let {
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
            _riskFactors.value = pregnant.listOfRiskFactors
            _notes.value = pregnant.notes
            _photo.value = pregnant.photo
        }
    }

    fun getPregnant(): Pregnant{
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
            ),
            notes = notes,
            photo = photo,
            riskFactors = riskFactors.split(",").map { RiskFactor(it) }

        )
    }

    private fun validateName(name: String):String?{
        if(name.isBlank()) return "No puede estar vacio"
        return null
    }

    private fun validateLastname(lastname: String): String?{
        if(lastname.isBlank()) return "No puede estar vacio"
        return null
    }

    private fun validateAge(age: String): String?{
        if(age.isBlank()) return null
        if(!age.isDigitsOnly()) return "Solo puede contener números"
        val ageDigit = age.toInt()
        if(ageDigit !in 5..60) return "Edad inválida"
        return null
    }

    private fun validateWeight(weight: String): String?{
        if(!weight.isDigitsOnly()) return "Solo puede contener números"
        return null
    }

    private fun validatePhone(phone: String): String?{
        if(phone.isBlank()) return "No puede estar vacio"
        val pattern = Patterns.PHONE
        val matcher = pattern.matcher(phone)
        if(!matcher.matches()) return "Numero de teléfono inválido"
        return null
    }

    private fun validateRiskFactors(riskFactors: String):String?{
        if(riskFactors.isBlank()) return "No puede estar vacio"
        val reg = "^[a-z]+((, | \\s)[a-z]+)*\$".toRegex()
        if(!reg.matches(riskFactors)) return "Deben estar separados por coma"
        return null
    }

    private fun validateSize(size: String): String?{
        if(size.isBlank()) return "No puede estar vacio"
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
    fun changeFumDate(fum : String){
        this._fum.value = fum
    }
    fun changeUsCalendarVisibility(isVisible: Boolean){
        _showUsCalendar.value = isVisible
    }
    fun changeFumCalendarVisibility (isVisible: Boolean){
        _showFumCalendar.value = isVisible
    }
    fun changeRiskFactors(riskFactors : String){
        this._riskFactors.value = riskFactors
        this.riskFactorsErrorMessage.value = validateRiskFactors(riskFactors)
    }
    fun changeNote(notes: String){
        this._notes.value = notes
    }
    fun onCheckboxChange(isChecked: Boolean){
        _isFumReliable.value = isChecked
    }

    fun changePhoto(photo: String){
        _photo.value = photo
    }
}