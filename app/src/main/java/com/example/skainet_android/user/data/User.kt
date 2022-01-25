package com.example.skainet_android.user.data

data class User(
    var id: String = "",
    var name: String = "",
    var trips: List<Trip>? = null
) {
    override fun toString(): String = "$id - $name - ${trips.toString()}"
}
