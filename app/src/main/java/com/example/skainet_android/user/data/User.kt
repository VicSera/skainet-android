package com.example.skainet_android.user.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "name") var name: String = "",

    @Ignore var trips: List<Trip>? = emptyList()
) {
    override fun toString(): String = name
}
