package com.devj.gestantescontrolcompose.ui.screens.editionscreen

import android.os.Parcelable
import androidx.compose.runtime.Composable
import com.devj.gestantescontrolcompose.ui.model.PregnantUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize


@Composable
fun rememberFormState( pregnant: PregnantUI? = null): FormState {

//    var state by rememberSaveable {
//        mutableStateOf(FormState(pregnant))
//    }
//    return state
    return FormState(pregnant)
}
@Parcelize
class FormState(private val pregnant: PregnantUI?): Parcelable {
    companion object{
        enum class FieldKey{
            NAME,
            LAST_NAME,
            AGE,
            SIZE,
            WEIGHT,
            PHONE,
            NOTES,
            RISK_FACTORS,
            FIRST_US,
            SECOND_US,
            THIRD_US,
            FUM,
            PHOTO,
        }
    }

    private  var _name  = MutableStateFlow(pregnant?.name ?: "")
    val name: StateFlow<String> get() = _name

    private var _lastName = MutableStateFlow(pregnant?.lastName ?: "")
    val lastName: StateFlow<String> get() = _lastName

    private var _age = MutableStateFlow(pregnant?.age ?: "")
    val age: StateFlow<String> get() = _age

    private var _size= MutableStateFlow(pregnant?.size ?: "")
    val size: StateFlow<String> get() = _size

    private var _weight = MutableStateFlow(pregnant?.weight ?: "")
    val weight: StateFlow<String> get() = _weight

    private var _phone = MutableStateFlow(pregnant?.phoneNumber ?: "")
    val phone: StateFlow<String> get() = _phone

    private var _notes = MutableStateFlow(pregnant?.notes ?: "")
    val notes: StateFlow<String> get() = _notes

    private var _riskFactors = MutableStateFlow(pregnant?.listOfRiskFactors ?: "")
    val riskFactors: StateFlow<String> get() = _riskFactors

    private var _firstUS = MutableStateFlow(pregnant?.firstUS ?: "")
    val firstUS: StateFlow<String> get() = _firstUS

    private var _secondUS = MutableStateFlow(pregnant?.secondUS ?: "")
    val secondUS: StateFlow<String> get() = _secondUS

    private var _thirdUS = MutableStateFlow(pregnant?.thirdUS ?: "")
    val thirdUS: StateFlow<String> get() = _thirdUS

    private var _fum = MutableStateFlow(pregnant?.fum ?: "")
    val fum: StateFlow<String> get() = _fum

    private var _isFUMReliable = MutableStateFlow(pregnant?.isFUMReliable ?: false)
    val isFUMReliable: StateFlow<Boolean> get() = _isFUMReliable

    private var _showFumCalendar= MutableStateFlow(false)
    val showFumCalendar: StateFlow<Boolean> get() = _showFumCalendar

    private var _showUsCalendar= MutableStateFlow(false)
    val showUsCalendar: StateFlow<Boolean> get() = _showUsCalendar

    private var _photo = MutableStateFlow(pregnant?.photo ?: "")
    val photo: StateFlow<String> get() = _photo



    fun changeInputValue(key: FieldKey ,newValue : String){
        val value = when(key){
            FieldKey.NAME -> _name
            FieldKey.LAST_NAME -> _lastName
            FieldKey.AGE -> _age
            FieldKey.SIZE -> _size
            FieldKey.WEIGHT -> _weight
            FieldKey.PHONE -> _phone
            FieldKey.NOTES -> _notes
            FieldKey.RISK_FACTORS -> _riskFactors
            FieldKey.FIRST_US -> _firstUS
            FieldKey.SECOND_US -> _secondUS
            FieldKey.THIRD_US -> _thirdUS
            FieldKey.FUM -> _fum
            FieldKey.PHOTO -> _photo
        }

        value.value = newValue
    }

    fun setReliability(value: Boolean) {
        _isFUMReliable.value = value
    }
    fun setFumCalendarVisibility(value: Boolean) {
        _showFumCalendar.value = value
    }

    fun setUsCalendarVisibility(value: Boolean) {
        _showUsCalendar.value = value
    }

}