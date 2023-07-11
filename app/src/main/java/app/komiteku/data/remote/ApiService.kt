package app.komiteku.data.remote

import app.komiteku.BuildConfig
import app.komiteku.utils.Constanta
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService {
    companion object {
        fun getApiMethod(): ApiMethod {
            val loggingInterceptor = if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(Constanta.CONNECT_TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(Constanta.WRITE_TIME_OUT, TimeUnit.MINUTES)
                .readTimeout(Constanta.READ_TIME_OUT, TimeUnit.MINUTES)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiMethod::class.java)
        }
    }
}
