package edu.ucne.vallagest.domain.vallas.repository

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.vallas.model.Valla
import kotlinx.coroutines.flow.Flow

interface VallaRepository {
    fun getVallas(): Flow<Resource<List<Valla>>>
    fun getValla(id: Int): Flow<Resource<Valla>>
    fun saveValla(valla: Valla): Flow<Resource<Valla>>
    fun updateValla(id: Int, valla: Valla): Flow<Resource<Unit>>
    fun deleteValla(id: Int): Flow<Resource<Unit>>
}