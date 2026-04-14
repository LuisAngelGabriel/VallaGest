package edu.ucne.vallagest

import app.cash.turbine.test
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.UploadResponseDto
import edu.ucne.vallagest.data.remotedatasource.VallaRemoteDataSource
import edu.ucne.vallagest.data.repository.UploadRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import okhttp3.MultipartBody

@OptIn(ExperimentalCoroutinesApi::class)
class UploadRepositoryImplTest {

    private lateinit var repository: UploadRepositoryImpl
    private val remoteDataSource: VallaRemoteDataSource = mockk()

    @Before
    fun setup() {
        repository = UploadRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `uploadImage emite Succes cuando la API responde correctamente`() = runTest {
        val mockFile = mockk<MultipartBody.Part>()
        val mockResponse = UploadResponseDto(url = "https://vallagest.com/img.jpg")

        coEvery { remoteDataSource.uploadImage(mockFile) } returns Result.success(mockResponse)

        repository.uploadImage(mockFile).test {
            assertTrue(awaitItem() is Resource.Loading)

            val result = awaitItem()
            assertTrue(result is Resource.Succes)
            assertEquals("https://vallagest.com/img.jpg", (result as Resource.Succes).data?.url)

            awaitComplete()
        }
    }

    @Test
    fun `uploadImage emite Error cuando falla la comunicacion con la API`() = runTest {
        val mockFile = mockk<MultipartBody.Part>()

        coEvery { remoteDataSource.uploadImage(mockFile) } returns Result.failure(Exception("Fallo de red"))

        repository.uploadImage(mockFile).test {
            assertTrue(awaitItem() is Resource.Loading)

            val result = awaitItem()
            assertTrue(result is Resource.Error)
            assertEquals("Fallo de red", (result as Resource.Error).message)

            awaitComplete()
        }
    }
}