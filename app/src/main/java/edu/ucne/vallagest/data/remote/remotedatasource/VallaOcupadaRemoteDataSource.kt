package edu.ucne.vallagest.data.remote.remotedatasource

import edu.ucne.vallagest.data.api.VallaApi
import edu.ucne.vallagest.data.remote.dto.VallaOcupadaDto
import javax.inject.Inject

class VallaOcupadaRemoteDataSource @Inject constructor(
    private val api: VallaApi
) {
    suspend fun getVallasOcupadas(): Result<List<VallaOcupadaDto>> {
        return try {
            val response = api.getVallasOcupadas()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}