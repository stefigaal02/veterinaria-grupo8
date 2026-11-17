package com.grupo08.pethealth.ui.historia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.grupo08.pethealth.data.remote.ApiClient
import com.grupo08.pethealth.data.remote.ConsultaResponse
import com.grupo08.pethealth.data.remote.DiagnosticoResponse
import com.grupo08.pethealth.data.remote.HistoriaResponse
import com.grupo08.pethealth.data.repository.HistoriaRepository
import com.grupo08.pethealth.utils.Resultado
import kotlinx.coroutines.launch

class HistoriaViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = HistoriaRepository(ApiClient.obtener(app))

    private val _historia = MutableLiveData<Resultado<HistoriaResponse>>()
    val historia: LiveData<Resultado<HistoriaResponse>> = _historia

    private val _consulta = MutableLiveData<Resultado<ConsultaResponse>>()
    val consulta: LiveData<Resultado<ConsultaResponse>> = _consulta

    private val _diagnostico = MutableLiveData<Resultado<DiagnosticoResponse>>()
    val diagnostico: LiveData<Resultado<DiagnosticoResponse>> = _diagnostico

    private val _resumen = MutableLiveData<Resultado<String>>()
    val resumen: LiveData<Resultado<String>> = _resumen

    fun cargar(mascotaId: Int) {
        _historia.value = Resultado.Cargando
        viewModelScope.launch { _historia.value = repo.obtener(mascotaId) }
    }

    fun abrir(mascotaId: Int) {
        _historia.value = Resultado.Cargando
        viewModelScope.launch { _historia.value = repo.abrir(mascotaId) }
    }

    fun guardarConsulta(historiaId: Int, motivo: String, tratamiento: String) {
        _consulta.value = Resultado.Cargando
        viewModelScope.launch { _consulta.value = repo.crearConsulta(historiaId, motivo, tratamiento) }
    }

    fun guardarDiagnostico(consultaId: Int, descripcion: String) {
        _diagnostico.value = Resultado.Cargando
        viewModelScope.launch { _diagnostico.value = repo.crearDiagnostico(consultaId, descripcion) }
    }

    fun cargarResumen(mascotaId: Int) {
        _resumen.value = Resultado.Cargando
        viewModelScope.launch { _resumen.value = repo.resumen(mascotaId) }
    }
}
