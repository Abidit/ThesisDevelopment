package DAO

import Model.User
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {
    @Insert
    suspend fun insertUser(user: User)

    @Query("select * from user where username=(:un) and password=(:ps)")
    suspend fun checkUser(un : String, ps : String) : User
}