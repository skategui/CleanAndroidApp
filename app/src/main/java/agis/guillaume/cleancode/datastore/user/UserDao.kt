package agis.guillaume.cleancode.datastore.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// SQL request to communicate with the local DB of the table users
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users where userID =:userID")
    suspend fun getUserByID(userID: Int): UserEntity?
}