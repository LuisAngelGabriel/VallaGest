package edu.ucne.vallagest

import app.cash.turbine.test
import edu.ucne.vallagest.data.local.dao.VallaOcupadaDao
import edu.ucne.vallagest.data.local.entities.VallaOcupadaEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.VallaOcupadaDto
import edu.ucne.vallagest.data.remote.remotedatasource.VallaOcupadaRemoteDataSource
import edu.ucne.vallagest.data.repository.VallaOcupadaRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VallaOcupadaRepositoryImplTest {

    private lateinit var repository: VallaOcupadaRepositoryImpl
    private val remoteDataSource: VallaOcupadaRemoteDataSource = mockk()
    private val vallaOcupadaDao: VallaOcupadaDao = mockk(relaxed = true)

    @Before
    fun setup() {
        repository = VallaOcupadaRepositoryImpl(remoteDataSource, vallaOcupadaDao)
    }

    @Test
    fun `getVallasOcupadas emite datos locales y luego actualiza desde el servidor`() = runTest {
        val localEntities = listOf(
            VallaOcupadaEntity(1, "Valla A", "Cliente X", "2024-01-01", "2024-02-01", 1500.0, 1)
        )
        val remoteDtos = listOf(
            VallaOcupadaDto(1, "Valla A", "Cliente X", "2024-01-01", "2024-02-01", 1500.0, 1)
        )

        coEvery { vallaOcupadaDao.observerAll().first() } returns localEntities
        coEvery { remoteDataSource.getVallasOcupadas() } returns Result.success(remoteDtos)
        every { vallaOcupadaDao.observerAll() } returns flowOf(localEntities)

        repository.getVallasOcupadas().test {
            assertTrue(awaitItem() is Resource.Loading)

            val initialSuccess = awaitItem()
            assertTrue(initialSuccess is Resource.Succes)
            assertEquals("Valla A", (initialSuccess as Resource.Succes).data?.first()?.nombreValla)

            coVerify { vallaOcupadaDao.clearAll() }
            coVerify { vallaOcupadaDao.upsertAll(any()) }

            val updatedSuccess = awaitItem()
            assertTrue(updatedSuccess is Resource.Succes)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getVallasOcupadas emite Error si la red falla y la base de datos local esta vacia`() = runTest {
        coEvery { vallaOcupadaDao.observerAll().first() } returns emptyList()
        coEvery { remoteDataSource.getVallasOcupadas() } returns Result.failure(Exception("Sin internet"))

        repository.getVallasOcupadas().test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Succes)

            val errorResult = awaitItem()
            assertTrue(errorResult is Resource.Error)
            assertEquals("Sin internet", (errorResult as Resource.Error).message)

            awaitComplete()
        }
    }
}