package com.example.data.di

import com.example.data.ApiService
import com.example.data.utils.Constant
import com.example.data.utils.KeyStorePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
/**NetworkModule is module class for network*/
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .client(client).baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideNasaApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        preference: KeyStorePreference,
    ): OkHttpClient {
//        val cacheSize = (5 * 1024 * 1024).toLong()
//        val mCache = Cache(context.cacheDir, cacheSize)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
//            .cache(mCache) // make your app offline-friendly without a database!
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .addInterceptor { chain ->
                var request = chain.request()
                request =
                    request.newBuilder().apply {
                        addHeader("Content-Type", "application/json")
                        addHeader("type", "Surveyor")
                        if (preference.getAuth() != null) {
                            addHeader("Authorization", "Bearer ${preference.getAuth().toString()}")
                        }
                    }.build()
                chain.proceed(request)
            }
        return client.build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }
}
