package agis.guillaume.cleancode.model

import java.util.*


data class Article(
    val source: String,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: Date
)