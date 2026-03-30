package edu.ucne.vallagest.data.api

import edu.ucne.vallagest.data.remote.dto.LoginRequest
import edu.ucne.vallagest.data.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import edu.ucne.vallagest.data.remote.dto.AuthResponse

interface AuthApi {
    @POST("api/Usuarios/Login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/Usuarios/Registrar")
    suspend fun registro(@Body request: RegisterRequest): Response<AuthResponse>
}