package agis.guillaume.cleancode.datastore.post

import androidx.room.*

// SQL request to communicate with the local DB of the table posts
@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPosts(post: List<PostEntity>)

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()

    @Query("SELECT * FROM posts ORDER BY postID")
    suspend fun getPosts(): List<PostEntity>
}