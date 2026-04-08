package edu.ucne.vallagest.data.remote.remotedatasource

import edu.ucne.vallagest.data.api.AuthApi
import edu.ucne.vallagest.data.remote.dto.AuthResponse
import edu.ucne.vallagest.data.remote.dto.LoginRequest
import edu.ucne.vallagest.data.remote.dto.RegisterRequest
import retrofit2.HttpException
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val api: AuthApi
) {
    suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = api.login(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception(e.response()?.errorBody()?.string() ?: "Error de servidor"))
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error desconocido"))
        }
    }

    suspend fun registro(request: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = api.registro(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al registrar usuario"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception(e.response()?.errorBody()?.string() ?: "Error de servidor"))
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error desconocido"))
        }
    }

    suspend fun actualizarUsuario(id: Int, request: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = api.actualizarUsuario(id, request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar perfil"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception(e.response()?.errorBody()?.string() ?: "Error de servidor"))
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error desconocido"))
        }
    }
}