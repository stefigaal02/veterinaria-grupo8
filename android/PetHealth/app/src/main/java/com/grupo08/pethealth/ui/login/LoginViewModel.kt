package com.grupo08.pethealth.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.grupo08.pethealth.data.remote.ApiClient
import com.grupo08.pethealth.data.remote.LoginResponse
import com.grupo08.pethealth.data.repository.AuthRepository
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = AuthRepository(ApiClient.obtener(app), SessionManager(app))

    private val _estado = MutableLiveData<Resultado<LoginResponse>>()
    val estado: LiveData<Resultado<LoginResponse>> = _estado

    fun login(email: String, password: String) {
        _estado.value = Resultado.Cargando
        viewModelScope.launch {
            _estado.value = repo.login(email, password)
        }
    }
}