package jp.chocofac.charlie.data.service.senryu

import android.os.Parcelable
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.chocofac.charlie.data.model.Param.Companion.BASE_URL
import kotlinx.parcelize.Parcelize
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

interface SenryuService {
    @Headers("content-type: application/json")
    @POST(".")
    suspend fun postImpressions(
        @Body impressions: Impressions
    ): Response<Senryu>
}

@Parcelize
data class Senryu(
    val first: String,
    val second: String,
    val last: String
) : Parcelable

data class Impressions(
    val impressions: String
)

@Module
@InstallIn(SingletonComponent::class)
class SenryuServiceModule {

    @Singleton
    @Provides
    fun provideLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor {
            Timber.tag("OkHttp").d(it)
        }.apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
    }

    @Singleton
    @Provides
    fun provideClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideSenryuService(retrofit: Retrofit): SenryuService {
        return retrofit.create(SenryuService::class.java)
    }
}