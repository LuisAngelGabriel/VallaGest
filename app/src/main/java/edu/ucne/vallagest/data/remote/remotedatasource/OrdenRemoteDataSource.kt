package edu.ucne.vallagest.data.remote.remotedatasource

import edu.ucne.vallagest.data.api.OrdenesApi
import edu.ucne.vallagest.data.remote.dto.CheckoutDto
import edu.ucne.vallagest.data.remote.dto.OrdenDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrdenRemoteDataSource @Inject constructor(
    private val api: OrdenesApi
) {
    suspend fun realizarCheckout(usuarioId: Int, meses: Int, dto: CheckoutDto): Result<Unit> {
        return try {
            val response = api.realizarCheckout(usuarioId, meses, dto)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("Error de servidor: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión a internet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHistorial(usuarioId: Int): Result<List<OrdenDto>> {
        return try {
            val response = api.getHistorialOrdenes(usuarioId)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(Exception("Error al cargar historial"))
        } catch (e: IOException) {
            Result.failure(Exception("Error de red"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelarOrden(ordenId: Int): Result<Unit> {
        return try {
            val response = api.cancelarOrden(ordenId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("No se pudo cancelar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}