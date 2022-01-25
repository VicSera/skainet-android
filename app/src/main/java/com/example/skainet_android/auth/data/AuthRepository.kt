package com.example.skainet_android.auth.data

import com.example.skainet_android.auth.data.remote.AuthDataSource
import com.example.skainet_android.core.Api

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
        val result = AuthDataSource.login(admin)
        if (result.isSuccess) {
            setLoggedInUser(admin, result.getOrThrow())
        }
        return result
    }

    private fun setLoggedInUser(admin: Admin, tokenHolder: TokenHolder) {
        AuthRepository.admin = admin
        Api.tokenInterceptor.token = tokenHolder.token
    }
}
