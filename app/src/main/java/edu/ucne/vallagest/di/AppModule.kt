package edu.ucne.vallagest.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.vallagest.data.api.AuthApi
import edu.ucne.vallagest.data.api.VallaApi
import edu.ucne.vallagest.data.db.VallaGestDb
import edu.ucne.vallagest.data.repository.AuthRepositoryImpl
import edu.ucne.vallagest.data.repository.VallaRepositoryImpl
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import edu.ucne.vallagest.domain.vallas.repository.VallaRepository
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://vallagest.somee.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideVallaApi(retrofit: Retrofit): VallaApi {
        return retrofit.create(VallaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVallaGestDb(@ApplicationContext context: Context): VallaGestDb {
        return Room.databaseBuilder(context, VallaGestDb::class.java, "VallaGest.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideVallaDao(db: VallaGestDb) = db.vallaDao()

    @Provides
    fun provideUsuarioDao(db: VallaGestDb) = db.usuarioDao()

    @Provides
    @Singleton
    fun provideVallaRepository(repository: VallaRepositoryImpl): VallaRepository = repository

    @Provides
    @Singleton
    fun provideAuthRepository(repository: AuthRepositoryImpl): AuthRepository = repository
}