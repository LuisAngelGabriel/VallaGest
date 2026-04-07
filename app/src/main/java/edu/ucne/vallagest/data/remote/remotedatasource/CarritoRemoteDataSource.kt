package edu.ucne.vallagest.data.remote.remotedatasource

import edu.ucne.vallagest.data.api.VallaApi
import edu.ucne.vallagest.data.remote.dto.CarritoItemDto
import edu.ucne.vallagest.data.remote.dto.CarritoPostDto
import retrofit2.HttpException
import javax.inject.Inject

class CarritoRemoteDataSource @Inject constructor(
    private val api: VallaApi
) {
    suspend fun getCarrito(usuarioId: Int): Result<List<CarritoItemDto>> {
        return try {
            val response = api.getCarrito(usuarioId)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(Exception(e.response()?.errorBody()?.string() ?: "Error de servidor"))
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error desconocido"))
        }
    }

    suspend fun agregarAlCarrito(item: CarritoPostDto): Result<Unit> {
        return try {
            val response = api.agregarAlCarrito(item)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarDelCarrito(id: Int): Result<Unit> {
        return try {
            val response = api.eliminarDelCarrito(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}