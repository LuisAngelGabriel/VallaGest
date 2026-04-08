package edu.ucne.vallagest.data.api

import edu.ucne.vallagest.data.remote.dto.LoginRequest
import edu.ucne.vallagest.data.remote.dto.RegisterRequest
import edu.ucne.vallagest.data.remote.dto.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {
    @POST("api/Usuarios/Login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/Usuarios/Registrar")
    suspend fun registro(@Body request: RegisterRequest): Response<AuthResponse>

    @PUT("api/Usuarios/Actualizar/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Int,
        @Body request: RegisterRequest
    ): Response<AuthResponse>
}