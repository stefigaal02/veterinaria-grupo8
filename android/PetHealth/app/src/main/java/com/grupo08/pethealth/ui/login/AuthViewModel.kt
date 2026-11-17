package com.grupo08.pethealth.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.grupo08.pethealth.data.remote.ApiClient
import com.grupo08.pethealth.data.remote.UsuarioResponse
import com.grupo08.pethealth.data.repository.AuthRepository
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.SessionManager
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = AuthRepository(ApiClient.obtener(app), SessionManager(app))

    private val _registro = MutableLiveData<Resultado<UsuarioResponse>>()
    val registro: LiveData<Resultado<UsuarioResponse>> = _registro

    private val _perfil = MutableLiveData<Resultado<UsuarioResponse>>()
    val perfil: LiveData<Resultado<UsuarioResponse>> = _perfil

    fun registrar(nombre: String, email: String, password: String, rol: String) {
        _registro.value = Resultado.Cargando
        viewModelScope.launch { _registro.value = repo.registro(nombre, email, password, rol) }
    }

    fun cargarPerfil() {
        _perfil.value = Resultado.Cargando
        viewModelScope.launch { _perfil.value = repo.perfil() }
    }
}
