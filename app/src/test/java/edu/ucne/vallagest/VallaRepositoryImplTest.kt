package edu.ucne.vallagest

import app.cash.turbine.test
import edu.ucne.vallagest.data.local.dao.VallaDao
import edu.ucne.vallagest.data.local.entities.VallaEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.VallaDto
import edu.ucne.vallagest.data.remotedatasource.VallaRemoteDataSource
import edu.ucne.vallagest.data.repository.VallaRepositoryImpl
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import edu.ucne.vallagest.domain.vallas.model.Valla
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VallaRepositoryImplTest {

    private lateinit var repository: VallaRepositoryImpl
    private val remoteDataSource: VallaRemoteDataSource = mockk()
    private val vallaDao: VallaDao = mockk(relaxed = true)
    private val authRepository: AuthRepository = mockk()

    @Before
    fun setup() {
        repository = VallaRepositoryImpl(remoteDataSource, vallaDao, authRepository)
    }

    @Test
    fun `getVallas emite datos locales y luego sincroniza con el servidor`() = runTest {
        val localVallas = listOf(
            VallaEntity(
                vallaId = 1,
                nombre = "Valla A",
                ubicacion = "Ubicacion",
                precioMensual = 1000.0,
                descripcion = "",
                estaOcupada = false,
                categoriaId = 1,
                imagenUrl = "",
                nombreCategoria = "Cat",
                isSynced = true
            )
        )
        val remoteDtos = listOf(
            VallaDto(
                vallaId = 1,
                nombre = "Valla A",
                ubicacion = "Ubicacion",
                precioMensual = 1000.0,
                descripcion = "",
                estaOcupada = false,
                categoriaId = 1,
                imagenUrl = "",
                nombreCategoria = "Cat"
            )
        )

        coEvery { vallaDao.observerAll().first() } returns localVallas
        coEvery { authRepository.getSession().firstOrNull() } returns null
        coEvery { remoteDataSource.getVallas() } returns Result.success(remoteDtos)
        every { vallaDao.observerAll() } returns flowOf(localVallas)

        repository.getVallas().test {
            assertTrue(awaitItem() is Resource.Loading)

            val initialSuccess = awaitItem() as Resource.Succes
            assertEquals("Valla A", initialSuccess.data?.first()?.nombre)

            coVerify { remoteDataSource.getVallas() }
            coVerify { vallaDao.clearAll() }
            coVerify { vallaDao.upsertAll(any()) }

            assertTrue(awaitItem() is Resource.Succes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `saveValla usa ID negativo localmente y lo reemplaza tras exito en API`() = runTest {
        val nuevaValla = Valla(
            vallaId = 0,
            nombre = "Nueva",
            ubicacion = "Ubicacion",
            precioMensual = 500.0,
            descripcion = "",
            estaOcupada = false,
            categoriaId = 1,
            imagenUrl = "",
            nombreCategoria = "Cat",
            isSynced = false
        )
        val dtoServer = VallaDto(
            vallaId = 100,
            nombre = "Nueva",
            ubicacion = "Ubicacion",
            precioMensual = 500.0,
            descripcion = "",
            estaOcupada = false,
            categoriaId = 1,
            imagenUrl = "",
            nombreCategoria = "Cat"
        )

        coEvery { authRepository.getSession().firstOrNull() } returns null
        coEvery { remoteDataSource.saveValla(any(), any()) } returns Result.success(dtoServer)

        repository.saveValla(nuevaValla).test {
            assertTrue(awaitItem() is Resource.Loading)

            val localResult = awaitItem() as Resource.Succes
            val tempId = localResult.data?.vallaId ?: 0
            assertTrue(tempId < 0)

            coVerify { vallaDao.deleteById(tempId) }
            coVerify { vallaDao.upsert(match { it.vallaId == 100 }) }

            assertTrue(awaitItem() is Resource.Succes)
            awaitComplete()
        }
    }

    @Test
    fun `updateValla marca como no sincronizado antes de llamar a la API`() = runTest {
        val valla = Valla(
            vallaId = 1,
            nombre = "Editada",
            ubicacion = "Ubicacion",
            precioMensual = 500.0,
            descripcion = "",
            estaOcupada = false,
            categoriaId = 1,
            imagenUrl = "",
            nombreCategoria = "Cat",
            isSynced = true
        )

        coEvery { authRepository.getSession().firstOrNull() } returns null
        coEvery { remoteDataSource.updateValla(any(), any(), any()) } returns Result.success(Unit)

        repository.updateValla(1, valla).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Succes)

            coVerify { vallaDao.upsert(match { it.vallaId == 1 && !it.isSynced }) }
            coVerify { vallaDao.upsert(match { it.vallaId == 1 && it.isSynced }) }

            awaitComplete()
        }
    }

    @Test
    fun `deleteValla elimina de Room de forma inmediata`() = runTest {
        coEvery { authRepository.getSession().firstOrNull() } returns null
        coEvery { remoteDataSource.deleteValla(any(), any()) } returns Result.success(Unit)

        repository.deleteValla(1).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Succes)

            coVerify { vallaDao.deleteById(1) }
            coVerify { remoteDataSource.deleteValla(1, any()) }

            awaitComplete()
        }
    }
}