package edu.ucne.vallagest.domain.vallas.repository

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.vallas.model.VallaOcupada
import kotlinx.coroutines.flow.Flow

interface VallaOcupadaRepository {
    fun getVallasOcupadas(): Flow<Resource<List<VallaOcupada>>>
}