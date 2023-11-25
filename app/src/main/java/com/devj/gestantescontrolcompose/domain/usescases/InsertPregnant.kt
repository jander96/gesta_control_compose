package com.devj.gestantescontrolcompose.domain.usescases


import com.devj.gestantescontrolcompose.domain.PregnantRepository
import com.devj.gestantescontrolcompose.domain.model.Pregnant
import com.devj.gestantescontrolcompose.domain.results.EditionEffect
import javax.inject.Inject


class InsertPregnant @Inject constructor(private val repo: PregnantRepository) {
    suspend operator fun invoke(pregnant: Pregnant): EditionEffect {
           return try {
               repo.insertPregnant(pregnant)
               EditionEffect.SuccessInsertion
           } catch (e:Exception){
               EditionEffect.InsertionError(e)
           }
    }
}