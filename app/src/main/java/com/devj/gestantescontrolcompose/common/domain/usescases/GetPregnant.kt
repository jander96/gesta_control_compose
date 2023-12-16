package com.devj.gestantescontrolcompose.common.domain.usescases

import com.devj.gestantescontrolcompose.common.domain.PregnantRepository
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPregnant @Inject constructor( private val  repo: PregnantRepository){
    suspend operator fun  invoke(id: Int): Result<Pregnant> = runCatching{
      withContext(Dispatchers.IO){
          repo.getPregnantById(id)
      }
    }
}