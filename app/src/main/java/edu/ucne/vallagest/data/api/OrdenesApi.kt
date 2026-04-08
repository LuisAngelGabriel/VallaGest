package edu.ucne.vallagest.data.api

import edu.ucne.vallagest.data.remote.dto.CheckoutDto
import edu.ucne.vallagest.data.remote.dto.OrdenDto
import retrofit2.Response
import retrofit2.http.*

interface OrdenesApi {

    @POST("api/Ordenes/Checkout/{usuarioId}/{meses}")
    suspend fun realizarCheckout(
        @Path("usuarioId") usuarioId: Int,
        @Path("meses") meses: Int,
        @Body checkoutDto: CheckoutDto
    ): Response<Unit?>

    @GET("api/Ordenes/Historial/{usuarioId}")
    suspend fun getHistorialOrdenes(
        @Path("usuarioId") usuarioId: Int
    ): List<OrdenDto>

    @DELETE("api/Ordenes/Cancelar/{ordenId}")
    suspend fun cancelarOrden(
        @Path("ordenId") ordenId: Int
    ): Response<Unit>
}