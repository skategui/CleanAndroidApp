package agis.guillaume.cleancode.datastore.post

import agis.guillaume.cleancode.model.User
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * User converter to be able to store and retrieve a user inside the field in the local DB.
 */
class UserTypeConverter {

    // to convert to json inside the field
   private val gson = Gson()

    @TypeConverter
    fun userToString(user: User): String {
        return gson.toJson(user)
    }

    @TypeConverter
    fun stringToUser(userString: String): User {
        val objectType = object : TypeToken<User>() {}.type
        return gson.fromJson(userString, objectType)
    }
}
