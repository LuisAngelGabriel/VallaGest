package edu.ucne.vallagest.data.remotedatasource

import edu.ucne.vallagest.data.api.CategoriaApi
import edu.ucne.vallagest.data.remote.dto.CategoriaDto
import retrofit2.HttpException
import javax.inject.Inject

class CategoriaRemoteDataSource @Inject constructor(
    private val api: CategoriaApi
) {
    suspend fun getCategorias(): Result<List<CategoriaDto>> {
        return try {
            val response = api.getCategorias()
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(Exception(e.response()?.errorBody()?.string() ?: "Error de servidor"))
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error desconocido"))
        }
    }

    suspend fun getCategoria(id: Int): Result<CategoriaDto> {
        return try {
            val response = api.getCategoria(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveCategoria(categoriaDto: CategoriaDto, rol: String): Result<CategoriaDto> {
        return try {
            val response = api.saveCategoria(categoriaDto, rol)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCategoria(id: Int, categoriaDto: CategoriaDto, rol: String): Result<Unit> {
        return try {
            val response = api.updateCategoria(id, categoriaDto, rol)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCategoria(id: Int, rol: String): Result<Unit> {
        return try {
            val response = api.deleteCategoria(id, rol)
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