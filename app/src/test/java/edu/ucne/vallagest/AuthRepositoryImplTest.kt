package edu.ucne.vallagest

import app.cash.turbine.test
import edu.ucne.vallagest.data.local.dao.UsuarioDao
import edu.ucne.vallagest.data.local.entities.UsuarioEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.AuthResponse
import edu.ucne.vallagest.data.remote.remotedatasource.AuthRemoteDataSource
import edu.ucne.vallagest.data.repository.AuthRepositoryImpl
import edu.ucne.vallagest.domain.usuarios.model.Usuario
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepositoryImpl
    private val remoteDataSource: AuthRemoteDataSource = mockk()
    private val usuarioDao: UsuarioDao = mockk(relaxed = true)

    @Before
    fun setup() {
        repository = AuthRepositoryImpl(remoteDataSource, usuarioDao)
    }

    @Test
    fun `login emite Succes y guarda en Room cuando la API responde bien`() = runTest {
        val loginResponse = AuthResponse(1, "Luis Morillo", "luis@gmail.com", "Admin")

        coEvery { remoteDataSource.login(any()) } returns Result.success(loginResponse)

        repository.login("luis@gmail.com", "1234").test {
            assertTrue(awaitItem() is Resource.Loading)

            val result = awaitItem()
            assertTrue(result is Resource.Succes)
            assertEquals("Luis Morillo", (result as Resource.Succes).data?.nombre)

            coVerify { usuarioDao.clearAll() }
            coVerify { usuarioDao.saveUsuario(any()) }

            awaitComplete()
        }
    }

    @Test
    fun `login emite Error cuando la API falla`() = runTest {
        coEvery { remoteDataSource.login(any()) } returns Result.failure(Exception("Error de red"))

        repository.login("luis@gmail.com", "1234").test {
            assertTrue(awaitItem() is Resource.Loading)

            val result = awaitItem()
            assertTrue(result is Resource.Error)
            assertEquals("Error de red", (result as Resource.Error).message)

            awaitComplete()
        }
    }

    @Test
    fun `registro emite Succes y persiste datos en local`() = runTest {
        val registerResponse = AuthResponse(2, "Angel Gabriel", "angel@gmail.com", "User")

        coEvery { remoteDataSource.registro(any()) } returns Result.success(registerResponse)

        repository.registro("Angel Gabriel", "angel@gmail.com", "password").test {
            assertTrue(awaitItem() is Resource.Loading)

            val result = awaitItem()
            assertTrue(result is Resource.Succes)
            assertEquals(2, (result as Resource.Succes).data?.usuarioId)

            coVerify { usuarioDao.clearAll() }
            coVerify { usuarioDao.saveUsuario(any()) }

            awaitComplete()
        }
    }

    @Test
    fun `actualizarUsuario guarda nueva informacion en Room`() = runTest {
        val updateResponse = AuthResponse(1, "Luis Editado", "luis@gmail.com", "Admin")

        coEvery { remoteDataSource.actualizarUsuario(any(), any()) } returns Result.success(updateResponse)

        repository.actualizarUsuario(1, mockk()).test {
            assertTrue(awaitItem() is Resource.Loading)

            val result = awaitItem()
            assertTrue(result is Resource.Succes)
            assertEquals("Luis Editado", (result as Resource.Succes).data?.nombre)

            coVerify { usuarioDao.saveUsuario(any()) }

            awaitComplete()
        }
    }
}