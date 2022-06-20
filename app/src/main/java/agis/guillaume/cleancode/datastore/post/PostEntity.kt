package agis.guillaume.cleancode.datastore.post

import agis.guillaume.cleancode.model.Post
import agis.guillaume.cleancode.model.User
import androidx.room.Entity
import androidx.room.PrimaryKey

// object stored in the local DB
@Entity(tableName = "posts")
class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val user: User?,
    val postID: Int,
    val title: String,
    val body: String
)


fun Post.toPostEntity() =
    PostEntity(
        postID = this.id,
        title = this.title,
        body = this.body,
        user = this.user
    )

fun PostEntity.toPost() =
    Post(
        id = this.postID,
        title = this.title,
        body = this.body,
        user = this.user
    )
