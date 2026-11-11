package com.grupo08.pethealth.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PetHealthApi {

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("api/auth/registro")
    suspend fun registro(@Body body: RegistroRequest): Response<UsuarioResponse>

    @GET("api/auth/perfil")
    suspend fun perfil(): Response<UsuarioResponse>
}