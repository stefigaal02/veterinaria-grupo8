package com.grupo08.pethealth.ui.pacientes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.grupo08.pethealth.data.remote.ApiClient
import com.grupo08.pethealth.data.remote.MascotaResponse
import com.grupo08.pethealth.data.remote.PropietarioResponse
import com.grupo08.pethealth.data.repository.MascotaRepository
import com.grupo08.pethealth.data.repository.PropietarioRepository
import com.grupo08.pethealth.utils.Resultado
import kotlinx.coroutines.launch

class RegistrarPacienteViewModel(app: Application) : AndroidViewModel(app) {

    private val api = ApiClient.obtener(app)
    private val propietarioRepo = PropietarioRepository(api)
    private val mascotaRepo = MascotaRepository(api)

    private val _propietarios = MutableLiveData<Resultado<List<PropietarioResponse>>>()
    val propietarios: LiveData<Resultado<List<PropietarioResponse>>> = _propietarios

    private val _propietarioCreado = MutableLiveData<Resultado<PropietarioResponse>>()
    val propietarioCreado: LiveData<Resultado<PropietarioResponse>> = _propietarioCreado

    private val _guardado = MutableLiveData<Resultado<MascotaResponse>>()
    val guardado: LiveData<Resultado<MascotaResponse>> = _guardado

    fun cargarPropietarios() {
        _propietarios.value = Resultado.Cargando
        viewModelScope.launch {
            _propietarios.value = propietarioRepo.listar()
        }
    }

    fun crearPropietario(documento: String, nombre: String, telefono: String) {
        _propietarioCreado.value = Resultado.Cargando
        viewModelScope.launch {
            _propietarioCreado.value = propietarioRepo.crear(documento, nombre, telefono)
        }
    }

    fun guardar(nombre: String, especie: String, raza: String, propietarioId: Int) {
        _guardado.value = Resultado.Cargando
        viewModelScope.launch {
            _guardado.value = mascotaRepo.crear(nombre, especie, raza, propietarioId)
        }
    }
}