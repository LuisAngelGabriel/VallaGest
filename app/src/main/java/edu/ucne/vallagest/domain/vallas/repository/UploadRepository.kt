package edu.ucne.vallagest.domain.vallas.repository

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.UploadResponseDto
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface UploadRepository {
    fun uploadImage(file: MultipartBody.Part): Flow<Resource<UploadResponseDto>>
}