package com.example.shopwithswipee.di

import android.content.Context
import com.example.shopwithswipee.data.ApiService.SwipeProductApiService
import com.example.shopwithswipee.data.repository.ProductServiceRepository
import com.example.shopwithswipee.data.repository.ProductServiceRepositoryImpl
import com.example.shopwithswipee.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDependencyModule {

    @Provides
    @Singleton
    fun provideSwipeProductServiceApi(
        @ApplicationContext context: Context
    ): SwipeProductApiService {
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.MINUTES)
            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(SwipeProductApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductServiceRepository(api: SwipeProductApiService): ProductServiceRepository {
        return ProductServiceRepositoryImpl(api)
    }


}