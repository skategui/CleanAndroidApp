package agis.guillaume.cleancode.datastore.article

import agis.guillaume.cleancode.model.Article
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

// object stored in the local DB
@Entity(tableName = "article")
class ArticleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val source: String,
    val author: String= "",
    val title: String= "",
    val description: String= "",
    val url: String,
    val urlToImage: String,
    val publishedAt: Date
)


fun Article.toArticleEntity() =
    ArticleEntity(
        author = this.author,
        title = this.title,
        source = this.source,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt
    )

fun ArticleEntity.toArticle() =
    Article(
        author = this.author,
        title = this.title,
        source = this.source,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt
    )
