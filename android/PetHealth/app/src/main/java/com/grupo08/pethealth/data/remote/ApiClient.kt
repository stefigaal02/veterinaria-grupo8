package com.grupo08.pethealth.data.remote

import android.content.Context
import com.grupo08.pethealth.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // 10.0.2.2 = el computador visto desde el emulador
    private const val BASE_URL = "http://127.0.0.1:5021/"

    private var api: PetHealthApi? = null

    fun obtener(context: Context): PetHealthApi {
        if (api == null) {
            val session = SessionManager(context.applicationContext)

            // Agrega "Authorization: Bearer <token>" a cada petición
            val authInterceptor = Interceptor { chain ->
                val token = session.token()
                val request = if (token != null) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    chain.request()
                }
                chain.proceed(request)
            }

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logging)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()

            api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PetHealthApi::class.java)
        }
        return api!!
    }
}