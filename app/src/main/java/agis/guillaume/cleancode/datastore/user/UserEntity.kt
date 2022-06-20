package agis.guillaume.cleancode.datastore.user

import agis.guillaume.cleancode.model.User
import androidx.room.Entity
import androidx.room.PrimaryKey

// object stored in the local DB
@Entity(tableName = "users")
class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userID: Int,
    val website: String,
    val username: String,
    val email: String,
    val phone: String
)


fun User.toUserEntity() =
    UserEntity(
        userID = this.id,
        website = this.website,
        username = this.username,
        email = this.email,
        phone = this.phone
    )

fun UserEntity.toUser() =
    User(
        id = this.userID,
        website = this.website,
        username = this.username,
        email = this.email,
        phone = this.phone
    )
