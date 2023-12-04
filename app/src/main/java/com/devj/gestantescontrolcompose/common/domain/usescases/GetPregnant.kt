package com.devj.gestantescontrolcompose.common.domain.usescases

import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import com.devj.gestantescontrolcompose.features.editor.domain.EditionEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPregnant @Inject constructor( private val  repo: PregnantRepository){
    suspend operator fun  invoke(id: Int): EditionEffect {
      return  withContext(Dispatchers.IO){
          try {
              val pregnant = repo.getPregnantById(id)
              return@withContext EditionEffect.PregnantFound(pregnant)
          } catch (e: Exception) {
             return@withContext EditionEffect.PregnantNotFound
          }
      }

    }
}