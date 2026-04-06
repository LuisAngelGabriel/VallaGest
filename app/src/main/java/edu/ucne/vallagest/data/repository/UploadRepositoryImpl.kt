package edu.ucne.vallagest.data.repository

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.UploadResponseDto
import edu.ucne.vallagest.data.remotedatasource.VallaRemoteDataSource
import edu.ucne.vallagest.domain.vallas.repository.UploadRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class UploadRepositoryImpl @Inject constructor(
    private val remoteDataSource: VallaRemoteDataSource
) : UploadRepository {

    override fun uploadImage(file: MultipartBody.Part): Flow<Resource<UploadResponseDto>> = flow {
        emit(Resource.Loading())

        remoteDataSource.uploadImage(file).onSuccess { dto ->
            emit(Resource.Succes(dto))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error al subir la imagen"))
        }
    }
}