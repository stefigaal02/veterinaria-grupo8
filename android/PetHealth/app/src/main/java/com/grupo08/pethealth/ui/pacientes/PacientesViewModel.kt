package com.grupo08.pethealth.ui.pacientes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.grupo08.pethealth.data.remote.ApiClient
import com.grupo08.pethealth.data.remote.MascotaResponse
import com.grupo08.pethealth.data.repository.MascotaRepository
import com.grupo08.pethealth.utils.Resultado
import kotlinx.coroutines.launch

class PacientesViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = MascotaRepository(ApiClient.obtener(app))

    private val _estado = MutableLiveData<Resultado<List<MascotaResponse>>>()
    val estado: LiveData<Resultado<List<MascotaResponse>>> = _estado

    fun cargar() {
        _estado.value = Resultado.Cargando
        viewModelScope.launch {
            _estado.value = repo.listar()
        }
    }
}