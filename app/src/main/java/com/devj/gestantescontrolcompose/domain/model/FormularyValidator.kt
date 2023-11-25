package com.devj.gestantescontrolcompose.domain.model

import com.devj.gestantescontrolcompose.domain.model.ValidatorException.NoAgeInRange
import com.devj.gestantescontrolcompose.domain.model.ValidatorException.NoFullNameCompleted
import com.devj.gestantescontrolcompose.domain.model.ValidatorException.NoFumSelected
import com.devj.gestantescontrolcompose.domain.results.EditionEffect
import javax.inject.Inject

class FormularyValidator @Inject constructor() {
    operator fun invoke(formulary: Formulary): EditionEffect {
        if (
            (formulary.age != null) &&
            (formulary.age.toInt() < 12) &&
            (formulary.age.toInt() > 50)
        ) return EditionEffect.ValidationError(NoAgeInRange)

        if (formulary.fUM == null && formulary.isFUMReliable) return EditionEffect.ValidationError(
            NoFumSelected
        )

        if (
            formulary.name.trim().isEmpty() and
            formulary.lastName.trim().isEmpty()
        ) return EditionEffect.ValidationError(NoFullNameCompleted)


        return EditionEffect.SuccessValidation
    }
}