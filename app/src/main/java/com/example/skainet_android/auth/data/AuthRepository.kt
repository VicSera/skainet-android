package com.example.skainet_android.auth.data

import com.example.skainet_android.auth.data.remote.RemoteAuthDataSource
import com.example.skainet_android.core.Api
import com.example.skainet_android.core.Result

object AuthRepository {
    var admin: Admin? = null
        private set

    val isLoggedIn: Boolean
        get() = admin != null

    init {
        admin = null
    }

    fun logout() {
        admin = null
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val admin = Admin(username, password)
        val result = RemoteAuthDataSource.login(admin)
        if (result is Result.Success<TokenHolder>) {
            setLoggedInAdmin(admin, result.data)
        }
        return result
    }

    private fun setLoggedInAdmin(admin: Admin, tokenHolder: TokenHolder) {
        AuthRepository.admin = admin
        Api.tokenInterceptor.token = tokenHolder.token
    }
}
