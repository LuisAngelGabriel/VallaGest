package edu.ucne.vallagest

import app.cash.turbine.test
import edu.ucne.vallagest.data.local.dao.CategoriaDao
import edu.ucne.vallagest.data.local.entities.CategoriaEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CategoriaDto
import edu.ucne.vallagest.data.remotedatasource.CategoriaRemoteDataSource
import edu.ucne.vallagest.data.repository.CategoriaRepositoryImpl
import edu.ucne.vallagest.domain.categorias.model.Categoria
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
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
class CategoriaRepositoryImplTest {

    private lateinit var repository: CategoriaRepositoryImpl
    private val remoteDataSource: CategoriaRemoteDataSource = mockk()
    private val categoriaDao: CategoriaDao = mockk(relaxed = true)
    private val authRepository: AuthRepository = mockk()

    @Before
    fun setup() {
        repository = CategoriaRepositoryImpl(remoteDataSource, categoriaDao, authRepository)
    }

    @Test
    fun `getCategorias emite datos locales y luego actualiza desde red`() = runTest {
        val localCategorias = listOf(CategoriaEntity(1, "Local", "Desc", true))
        val remoteDtos = listOf(CategoriaDto(1, "Remoto", "Desc"))

        coEvery { categoriaDao.observerAll().first() } returns localCategorias
        coEvery { authRepository.getSession().firstOrNull() } returns null
        coEvery { remoteDataSource.getCategorias() } returns Result.success(remoteDtos)
        every { categoriaDao.observerAll() } returns flowOf(localCategorias)

        repository.getCategorias().test {
            assertTrue(awaitItem() is Resource.Loading)

            val initialSucces = awaitItem()
            assertTrue(initialSucces is Resource.Succes)
            assertEquals("Local", (initialSucces as Resource.Succes).data?.first()?.nombre)

            coVerify { remoteDataSource.getCategorias() }
            coVerify { categoriaDao.upsertAll(any()) }

            val finalSucces = awaitItem()
            assertTrue(finalSucces is Resource.Succes)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `saveCategoria guarda local con ID negativo y limpia tras exito de API`() = runTest {
        val nuevaCategoria = Categoria(0, "Test", "Desc", false)
        val categoriaDtoServer = CategoriaDto(50, "Test", "Desc")

        coEvery { authRepository.getSession().firstOrNull() } returns null
        coEvery { remoteDataSource.saveCategoria(any(), any()) } returns Result.success(categoriaDtoServer)

        repository.saveCategoria(nuevaCategoria).test {
            assertTrue(awaitItem() is Resource.Loading)

            val localResult = awaitItem() as Resource.Succes
            val tempId = localResult.data?.categoriaId ?: 0

            assertTrue(tempId < 0)

            coVerify { categoriaDao.deleteById(tempId) }
            coVerify { categoriaDao.upsert(match { it.categoriaId == 50 }) }

            assertTrue(awaitItem() is Resource.Succes)
            awaitComplete()
        }
    }

    @Test
    fun `updateCategoria actualiza local e intenta sincronizar`() = runTest {
        val categoria = Categoria(1, "Editado", "Desc", false)

        coEvery { authRepository.getSession().firstOrNull() } returns null
        coEvery { remoteDataSource.updateCategoria(any(), any(), any()) } returns Result.success(Unit)

        repository.updateCategoria(1, categoria).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Succes)

            coVerify { categoriaDao.upsert(match { it.categoriaId == 1 && !it.isSynced }) }
            coVerify { categoriaDao.upsert(match { it.categoriaId == 1 && it.isSynced }) }

            assertTrue(awaitItem() is Resource.Succes)
            awaitComplete()
        }
    }

    @Test
    fun `deleteCategoria borra localmente de inmediato`() = runTest {
        coEvery { authRepository.getSession().firstOrNull() } returns null
        coEvery { remoteDataSource.deleteCategoria(any(), any()) } returns Result.success(Unit)

        repository.deleteCategoria(1).test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Succes)

            coVerify { categoriaDao.deleteById(1) }
            coVerify { remoteDataSource.deleteCategoria(1, any()) }

            awaitComplete()
        }
    }
}