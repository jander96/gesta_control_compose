package com.devj.gestantescontrolcompose.domain.usescases


import android.util.Log
import com.devj.gestantescontrolcompose.domain.PregnantRepository
import com.devj.gestantescontrolcompose.domain.model.Pregnant
import com.devj.gestantescontrolcompose.domain.results.EditionEffect
import javax.inject.Inject


class InsertPregnant @Inject constructor(private val repo: PregnantRepository) {
    suspend operator fun invoke(pregnant: Pregnant): EditionEffect {
           return try {
               Log.d("Insert","Se manda solicitud al repo")
               repo.insertPregnant(pregnant)
               Log.d("Insert","Se insertó gestante")
               EditionEffect.SuccessInsertion
           } catch (e:Exception){
               EditionEffect.ErrorInsertion(e)
           }
    }
}