package com.grupo08.pethealth.ui.vacunas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.grupo08.pethealth.data.remote.ApiClient
import com.grupo08.pethealth.data.remote.AplicacionResponse
import com.grupo08.pethealth.data.remote.VacunaResponse
import com.grupo08.pethealth.data.repository.VacunaRepository
import com.grupo08.pethealth.utils.Resultado
import kotlinx.coroutines.launch

class VacunaViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = VacunaRepository(ApiClient.obtener(app))

    private val _vacunas = MutableLiveData<Resultado<List<VacunaResponse>>>()
    val vacunas: LiveData<Resultado<List<VacunaResponse>>> = _vacunas

    private val _aplicaciones = MutableLiveData<Resultado<List<AplicacionResponse>>>()
    val aplicaciones: LiveData<Resultado<List<AplicacionResponse>>> = _aplicaciones

    private val _guardado = MutableLiveData<Resultado<AplicacionResponse>>()
    val guardado: LiveData<Resultado<AplicacionResponse>> = _guardado

    private val _vacunaCreada = MutableLiveData<Resultado<VacunaResponse>>()
    val vacunaCreada: LiveData<Resultado<VacunaResponse>> = _vacunaCreada

    private val _proximas = MutableLiveData<Resultado<List<AplicacionResponse>>>()
    val proximas: LiveData<Resultado<List<AplicacionResponse>>> = _proximas

    fun cargarVacunas() {
        viewModelScope.launch { _vacunas.value = repo.vacunas() }
    }

    fun cargarAplicaciones(mascotaId: Int) {
        _aplicaciones.value = Resultado.Cargando
        viewModelScope.launch { _aplicaciones.value = repo.aplicaciones(mascotaId) }
    }

    fun aplicar(mascotaId: Int, vacunaId: Int, fecha: String, refuerzo: String) {
        _guardado.value = Resultado.Cargando
        viewModelScope.launch { _guardado.value = repo.aplicar(mascotaId, vacunaId, fecha, refuerzo) }
    }

    fun crearVacuna(nombre: String) {
        viewModelScope.launch { _vacunaCreada.value = repo.crearVacuna(nombre) }
    }

    fun cargarProximas(dias: Int) {
        _proximas.value = Resultado.Cargando
        viewModelScope.launch { _proximas.value = repo.proximas(dias) }
    }
}
