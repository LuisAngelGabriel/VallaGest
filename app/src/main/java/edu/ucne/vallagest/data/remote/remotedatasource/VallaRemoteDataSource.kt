package edu.ucne.vallagest.data.remotedatasource

import edu.ucne.vallagest.data.api.VallaApi
import edu.ucne.vallagest.data.remote.dto.VallaDto
import edu.ucne.vallagest.data.remote.dto.UploadResponseDto
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject

class VallaRemoteDataSource @Inject constructor(
    private val api: VallaApi
) {
    suspend fun getVallas(): Result<List<VallaDto>> {
        return try {
            val response = api.getVallas()
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(Exception(e.response()?.errorBody()?.string() ?: "Error de servidor"))
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error desconocido"))
        }
    }

    suspend fun getValla(id: Int): Result<VallaDto> {
        return try {
            val response = api.getValla(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveValla(vallaDto: VallaDto, rol: String): Result<VallaDto> {
        return try {
            val response = api.postValla(vallaDto, rol)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateValla(id: Int, vallaDto: VallaDto, rol: String): Result<Unit> {
        return try {
            val response = api.putValla(id, vallaDto, rol)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.errorBody()?.string() ?: "Error ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteValla(id: Int, rol: String): Result<Unit> {
        return try {
            val response = api.deleteValla(id, rol)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.errorBody()?.string() ?: "Error ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadImage(file: MultipartBody.Part): Result<UploadResponseDto> {
        return try {
            val response = api.uploadImage(file)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}