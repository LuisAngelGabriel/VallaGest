package edu.ucne.vallagest.data.api

import edu.ucne.vallagest.data.remote.dto.CategoriaDto
import retrofit2.Response
import retrofit2.http.*

interface CategoriaApi {
    @GET("api/Categorias")
    suspend fun getCategorias(): List<CategoriaDto>

    @GET("api/Categorias/{id}")
    suspend fun getCategoria(@Path("id") id: Int): Response<CategoriaDto>

    @POST("api/Categorias")
    suspend fun saveCategoria(
        @Body categoriaDto: CategoriaDto,
        @Header("Rol") rol: String
    ): Response<CategoriaDto>

    @PUT("api/Categorias/{id}")
    suspend fun updateCategoria(
        @Path("id") id: Int,
        @Body categoriaDto: CategoriaDto,
        @Header("Rol") rol: String
    ): Response<Unit>

    @DELETE("api/Categorias/{id}")
    suspend fun deleteCategoria(
        @Path("id") id: Int,
        @Header("Rol") rol: String
    ): Response<Unit>
}