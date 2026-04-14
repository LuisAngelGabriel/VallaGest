package edu.ucne.vallagest

import app.cash.turbine.test
import edu.ucne.vallagest.data.local.dao.CarritoDao
import edu.ucne.vallagest.data.local.dao.OrdenDao
import edu.ucne.vallagest.data.local.entities.OrdenEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CheckoutDto
import edu.ucne.vallagest.data.remote.dto.OrdenDto
import edu.ucne.vallagest.data.remote.remotedatasource.OrdenRemoteDataSource
import edu.ucne.vallagest.data.repository.OrdenRepositoryImpl
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrdenRepositoryImplTest {

    private lateinit var repository: OrdenRepositoryImpl
    private val remoteDataSource: OrdenRemoteDataSource = mockk()
    private val ordenDao: OrdenDao = mockk(relaxed = true)
    private val carritoDao: CarritoDao = mockk(relaxed = true)

    @Before
    fun setup() {
        repository = OrdenRepositoryImpl(remoteDataSource, ordenDao, carritoDao)
    }

    @Test
    fun `realizarPago emite Succes y limpia carrito cuando la API responde bien`() = runTest {
        val checkoutDto = CheckoutDto(metodo = 1, comprobanteUrl = "")
        coEvery { remoteDataSource.realizarCheckout(any(), any(), any()) } returns Result.success(Unit)

        repository.realizarPago(1, 12, checkoutDto).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Succes)

            coVerify { carritoDao.clearCarrito() }
            awaitComplete()
        }
    }

    @Test
    fun `getHistorial emite datos de Room tras sincronizar con la API`() = runTest {
        val usuarioId = 1
        val ordenesDto = listOf(
            OrdenDto(
                ordenId = 1,
                fechaOrden = "2024-04-14",
                total = 5000.0,
                metodo = "Tarjeta",
                estado = "Pagado",
                comprobanteUrl = "",
                detalles = emptyList()
            )
        )
        val ordenesEntity = listOf(
            OrdenEntity(
                ordenId = 1,
                usuarioId = usuarioId,
                fechaOrden = "2024-04-14",
                total = 5000.0,
                metodo = "Tarjeta",
                estado = "Pagado",
                comprobanteUrl = ""
            )
        )

        coEvery { remoteDataSource.getHistorial(usuarioId) } returns Result.success(ordenesDto)
        coEvery { ordenDao.getOrdenes(usuarioId).first() } returns ordenesEntity
        coEvery { ordenDao.getDetallesByOrden(1) } returns emptyList()

        repository.getHistorial(usuarioId).test {
            assertTrue(awaitItem() is Resource.Loading)

            val result = awaitItem()
            assertTrue(result is Resource.Succes)
            assertEquals(1, (result as Resource.Succes).data?.size)

            coVerify { ordenDao.clearOrdenes(usuarioId) }
            coVerify { ordenDao.insertOrden(any()) }

            awaitComplete()
        }
    }

    @Test
    fun `cancelarOrden emite Succes cuando la API responde bien`() = runTest {
        coEvery { remoteDataSource.cancelarOrden(any()) } returns Result.success(Unit)

        repository.cancelarOrden(1).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Succes)
            awaitComplete()
        }
    }

    @Test
    fun `cancelarOrden emite Error cuando la API falla`() = runTest {
        coEvery { remoteDataSource.cancelarOrden(any()) } returns Result.failure(Exception("No se pudo cancelar"))

        repository.cancelarOrden(1).test {
            assertTrue(awaitItem() is Resource.Loading)

            val result = awaitItem()
            assertTrue(result is Resource.Error)
            assertEquals("No se pudo cancelar", (result as Resource.Error).message)

            awaitComplete()
        }
    }
}