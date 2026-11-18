package com.grupo08.pethealth.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.ResponseBody

interface PetHealthApi {

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("api/auth/registro")
    suspend fun registro(@Body body: RegistroRequest): Response<UsuarioResponse>

    @GET("api/auth/perfil")
    suspend fun perfil(): Response<UsuarioResponse>

    @GET("api/mascotas")
    suspend fun mascotas(): Response<List<MascotaResponse>>

    @GET("api/propietarios")
    suspend fun propietarios(): Response<List<PropietarioResponse>>

    @POST("api/propietarios")
    suspend fun crearPropietario(@Body body: CrearPropietarioRequest): Response<PropietarioResponse>

    @POST("api/mascotas")
    suspend fun crearMascota(@Body body: CrearMascotaRequest): Response<MascotaResponse>

    @GET("api/historias/{id}")
    suspend fun historia(@Path("id") id: Int): Response<HistoriaResponse>

    @POST("api/historias")
    suspend fun abrirHistoria(@Body body: AbrirHistoriaRequest): Response<HistoriaResponse>

    @GET("api/historias/{id}/resumen")
    suspend fun resumen(@Path("id") id: Int): Response<ResponseBody>

    @POST("api/consultas")
    suspend fun crearConsulta(@Body body: CrearConsultaRequest): Response<ConsultaResponse>

    @POST("api/diagnosticos")
    suspend fun crearDiagnostico(@Body body: CrearDiagnosticoRequest): Response<DiagnosticoResponse>

    @GET("api/vacunas")
    suspend fun vacunas(): Response<List<VacunaResponse>>

    @POST("api/vacunas")
    suspend fun crearVacuna(@Body body: CrearVacunaRequest): Response<VacunaResponse>

    @POST("api/vacunas/aplicaciones")
    suspend fun aplicarVacuna(@Body body: AplicarVacunaRequest): Response<AplicacionResponse>

    @GET("api/vacunas/aplicaciones/{id}")
    suspend fun aplicaciones(@Path("id") id: Int): Response<List<AplicacionResponse>>

    @GET("api/vacunas/proximas")
    suspend fun proximas(@Query("dias") dias: Int): Response<List<AplicacionResponse>>

    @GET("api/mascotas/{id}")
    suspend fun mascota(@Path("id") id: Int): Response<MascotaResponse>

    @PUT("api/mascotas/{id}")
    suspend fun actualizarMascota(@Path("id") id: Int, @Body body: CrearMascotaRequest): Response<MascotaResponse>

    @PUT("api/mascotas/{id}/anular")
    suspend fun anularMascota(@Path("id") id: Int): Response<MensajeResponse>

    @PUT("api/mascotas/{id}/reactivar")
    suspend fun reactivarMascota(@Path("id") id: Int): Response<MensajeResponse>

    @GET("api/mascotas/anulados")
    suspend fun mascotasAnuladas(): Response<List<MascotaResponse>>
}
