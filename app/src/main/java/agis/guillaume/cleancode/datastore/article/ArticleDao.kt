package agis.guillaume.cleancode.datastore.article

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// SQL request to communicate with the local DB of the table article
@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArticles(post: List<ArticleEntity>)

    @Query("DELETE FROM article")
    suspend fun deleteAllArticles()

    @Query("SELECT * FROM article")
     fun getAllArticles(): Flow<List<ArticleEntity>> // usage of flow to automatially emit the latest version when the table is updated
}