package agis.guillaume.cleancode.api.model

import agis.guillaume.cleancode.model.Article
import com.google.gson.annotations.SerializedName
import java.util.*

data class ArticlesResponse(@SerializedName("articles") val articles: List<ArticleResponse>)

data class ArticleResponse(
    @SerializedName("source") val source: SourceResponse,
    @SerializedName("author") val author: String? = null,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String? = null,
    @SerializedName("publishedAt") val publishedAt: Date
)

data class SourceResponse(@SerializedName("name") val name: String)

fun ArticleResponse.isValid() = author != null && description != null && urlToImage != null

fun ArticleResponse.toArticle() = Article(
    author = this.author?:"",
    title = this.title,
    source = this.source.name,
    description = this.description?:"",
    url = this.url,
    urlToImage = this.urlToImage?:"",
    publishedAt = this.publishedAt
)