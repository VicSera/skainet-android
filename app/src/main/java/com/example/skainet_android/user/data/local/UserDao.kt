package com.example.skainet_android.user.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.skainet_android.user.data.User

@Dao
interface UserDao {
    @Query("SELECT * from users ORDER BY name ASC")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE id=:id ")
    fun getById(id: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}