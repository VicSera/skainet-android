package com.example.skainet_android.user.userEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditViewModelFactory(private val arg: String?): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(String::class.java).newInstance(arg)
    }
}