package edu.ucne.vallagest.data.api

import edu.ucne.vallagest.data.remote.dto.VallaDto
import edu.ucne.vallagest.data.remote.dto.UploadResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface VallaApi {
    @GET("api/Vallas")
    suspend fun getVallas(): List<VallaDto>

    @GET("api/Vallas/{id}")
    suspend fun getValla(@Path("id") id: Int): VallaDto

    @POST("api/Vallas")
    suspend fun postValla(
        @Body vallaDto: VallaDto,
        @Header("Rol") rol: String
    ): VallaDto

    @PUT("api/Vallas/{id}")
    suspend fun putValla(
        @Path("id") id: Int,
        @Body vallaDto: VallaDto,
        @Header("Rol") rol: String
    ): Response<Unit>

    @DELETE("api/Vallas/{id}")
    suspend fun deleteValla(
        @Path("id") id: Int,
        @Header("Rol") rol: String
    ): Response<Unit>

    @Multipart
    @POST("api/Upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): UploadResponseDto
}