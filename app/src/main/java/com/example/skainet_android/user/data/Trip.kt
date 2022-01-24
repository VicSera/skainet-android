package com.example.skainet_android.user.data

data class Trip(
    var id: String = "",
    var userId: String = "",
    var name: String = "",
    var datetime: String = ""
) {
    override fun toString(): String {
        return "$id - $name"
    }
}