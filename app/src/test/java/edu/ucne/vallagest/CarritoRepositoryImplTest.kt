package edu.ucne.vallagest

import app.cash.turbine.test
import edu.ucne.vallagest.data.local.dao.CarritoDao
import edu.ucne.vallagest.data.local.dao.VallaDao
import edu.ucne.vallagest.data.local.entities.CarritoEntity
import edu.ucne.vallagest.data.local.entities.VallaEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CarritoItemDto
import edu.ucne.vallagest.data.remote.dto.CarritoPostDto
import edu.ucne.vallagest.data.remote.remotedatasource.CarritoRemoteDataSource
import edu.ucne.vallagest.data.repository.CarritoRepositoryImpl
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CarritoRepositoryImplTest {

    private lateinit var repository: CarritoRepositoryImpl
    private val remoteDataSource: CarritoRemoteDataSource = mockk()
    private val carritoDao: CarritoDao = mockk(relaxed = true)
    private val vallaDao: VallaDao = mockk()
    private val authRepository: AuthRepository = mockk()

    @Before
    fun setup() {
        repository = CarritoRepositoryImpl(remoteDataSource, carritoDao, vallaDao, authRepository)
    }

    @Test
    fun `getCarrito emite local e intenta sincronizar pendientes`() = runTest {
        val usuarioId = 1
        val localItems = listOf(
            CarritoEntity(
                carritoItemId = 10,
                vallaId = 1,
                nombreValla = "Valla A",
                precio = 500.0,
                imagenUrl = "url_imagen",
                isSynced = false
            )
        )
        val remoteItemsDto = listOf(
            CarritoItemDto(
                carritoItemId = 1,
                vallaId = 1,
                nombreValla = "Valla A",
                precio = 500.0,
                imagenUrl = "url_imagen"
            )
        )

        coEvery { carritoDao.getCarritoLocal().first() } returns localItems
        coEvery { remoteDataSource.agregarAlCarrito(any()) } returns Result.success(Unit)
        coEvery { remoteDataSource.getCarrito(usuarioId) } returns Result.success(remoteItemsDto)
        coEvery { carritoDao.getCarritoLocal() } returns flowOf(localItems)

        repository.getCarrito(usuarioId).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Succes)

            coVerify { remoteDataSource.agregarAlCarrito(any()) }
            coVerify { carritoDao.deleteById(10) }
            coVerify { carritoDao.clearCarrito() }
            coVerify { carritoDao.insertCarrito(any()) }

            assertTrue(awaitItem() is Resource.Succes)
            awaitComplete()
        }
    }

    @Test
    fun `agregarAlCarrito guarda local con ID negativo y luego sincroniza`() = runTest {
        val dto = CarritoPostDto(1, 1)
        val valla = VallaEntity(
            vallaId = 1,
            nombre = "Valla A",
            ubicacion = "Ubicacion",
            precioMensual = 500.0,
            descripcion = "Desc",
            imagenUrl = "url_imagen",
            estaOcupada = false,
            categoriaId = 1,
            nombreCategoria = "Cat",
            isSynced = true
        )
        val remoteItemsDto = listOf(
            CarritoItemDto(
                carritoItemId = 100,
                vallaId = 1,
                nombreValla = "Valla A",
                precio = 500.0,
                imagenUrl = "url_imagen"
            )
        )

        coEvery { vallaDao.getById(1) } returns valla
        coEvery { remoteDataSource.agregarAlCarrito(dto) } returns Result.success(Unit)
        coEvery { remoteDataSource.getCarrito(1) } returns Result.success(remoteItemsDto)

        repository.agregarAlCarrito(dto).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Succes)

            coVerify { carritoDao.insertCarrito(match { it.first().carritoItemId < 0 }) }
            coVerify { carritoDao.deleteById(any()) }
            coVerify { carritoDao.insertCarrito(any()) }

            awaitComplete()
        }
    }

    @Test
    fun `eliminarDelCarrito borra localmente de inmediato`() = runTest {
        val itemId = 10
        coEvery { remoteDataSource.eliminarDelCarrito(itemId) } returns Result.success(Unit)

        repository.eliminarDelCarrito(itemId).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Succes)

            coVerify { carritoDao.deleteById(itemId) }
            coVerify { remoteDataSource.eliminarDelCarrito(itemId) }

            awaitComplete()
        }
    }
}