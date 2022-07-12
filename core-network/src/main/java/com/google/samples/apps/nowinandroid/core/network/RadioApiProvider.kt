package com.dmhsh.samples.apps.nowinandroid.core.network


import com.dmhsh.samples.apps.nowinandroid.core.network.retrofit.RadioListApi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RadioApiProvider {

    @Provides
    @Singleton
    fun provideAuthInterceptorOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

//    @Provides
//    @Singleton
//    fun provideRetrofit(
//        okHttpClient: OkHttpClient
//    ): Retrofit {
//        return Retrofit
//            .Builder()
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(API_URL)
//            .build()
//    }

    @Provides
    @Singleton
    fun provideCountryListApiService(
        retrofit: Retrofit
    ): RadioListApi.Service {
        return retrofit.create(RadioListApi.Service::class.java)
    }

}