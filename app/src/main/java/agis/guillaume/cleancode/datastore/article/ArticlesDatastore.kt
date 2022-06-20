package agis.guillaume.cleancode.datastore.article

import agis.guillaume.cleancode.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * ArticlesLocalDatastore is responsible to store the articles into the local DB (using android Room)
 */
internal class ArticlesLocalDatastore(private val articleDao: ArticleDao) : ArticlesDatastore {

    /**
     * Clean the DB and stored the latest version of the articles
     *  @param articles list of articles to store
     */
    override suspend fun saveArticles(articles: List<Article>) {
        articleDao.deleteAllArticles()
        articleDao.insertAllArticles(articles.map { article -> article.toArticleEntity() })
    }

    /**
     * Get all the stored articles
     *  @return [Flow] [List] [Article]  list of posts fetched from server
     */
    override fun getArticles(): Flow<List<Article>> =
        articleDao.getAllArticles().map { it.map { entity -> entity.toArticle() } }
}

/**
 * ArticlesDatastore is is an interface to hide the implementation of the ArticlesLocalDatastore.
 * PS : it's a bit over engineered for this app, but useful for the unit tests :)
 */
interface ArticlesDatastore {
    suspend fun saveArticles(articles: List<Article>)
    fun getArticles(): Flow<List<Article>>
}