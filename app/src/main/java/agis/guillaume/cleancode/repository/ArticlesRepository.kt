package agis.guillaume.cleancode.repository

import agis.guillaume.cleancode.BuildConfig
import agis.guillaume.cleancode.api.services.ArticlesService
import agis.guillaume.cleancode.model.Article
import agis.guillaume.cleancode.api.model.isValid
import agis.guillaume.cleancode.api.model.toArticle

/**
 * ArticlesRepository is responsible to make the HTTP request to the server in order to load the articles
 * (using retrofit)
 */
class ArticlesRepository(private val service: ArticlesService) : IArticlesRepository {

    /**
     * Get the list of articles from the server for a given page. Filter the response to not take
     * in consideration the article with missing informations
     *  @return [List] [Article] Response from the server, containing the list of articles if success
     */
    override suspend fun getBusinessArticles(key: String) =
        service.getBusinessArticles(key)
            .body()!!.articles
            .filter { it.isValid() } // does not keep the ones who have missing info
            .map { it.toArticle() } // convert into the object we are going to manipulate, with less attributes
}

/**
 * IArticlesRepository is is an interface to hide the implementation of the repository.
 * PS : it's a bit over engineered for this interface, but useful for the unit tests :)
 */
interface IArticlesRepository {
    suspend fun getBusinessArticles(key : String): List<Article>
}