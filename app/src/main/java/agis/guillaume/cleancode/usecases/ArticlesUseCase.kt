package agis.guillaume.cleancode.usecases

import agis.guillaume.cleancode.api.utils.ResultOf
import agis.guillaume.cleancode.datastore.article.ArticlesDatastore
import agis.guillaume.cleancode.model.Article
import agis.guillaume.cleancode.model.Post
import agis.guillaume.cleancode.repository.IArticlesRepository
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

/**
 * ArticlesUseCase is responsible to manage all the business logic related to the articles
 * No UI logic will be found here
 */
class ArticlesUseCase(
    private val articlesRepository: IArticlesRepository,
    private val articleDatastore: ArticlesDatastore,
    private val apiKey: String,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO // its a good practice to have the dispatcher as a param, as it's also useful for the unit tests
) {

    /**
     * Load articles from the server and store them in the local DB
     */
    suspend fun loadArticles(): Flow<ResultOf<Unit>> {
        return if (apiKey == API_KEY_NOT_ADDED) {
            flowOf(ResultOf.Failure(message = API_KEY_ERROR_MSG))
        } else
            flow {
                try {
                    val articles = articlesRepository.getBusinessArticles(apiKey)
                    emit(ResultOf.Success(Unit))
                    articleDatastore.saveArticles(articles)
                } catch (e: Exception) {
                    emit(ResultOf.Failure(exception = e))
                }
            }.flowOn(ioDispatcher)
    }

    /**
     * Get the posts list from the local DB. As soon as the local DB is updated,
     * then this flow with emit the latest version
     *  @return [Flow] [List] [Post]  list of posts fetched from server
     */
    fun getLoadedArticles(): Flow<List<Article>> {
        return articleDatastore.getArticles()
    }

    companion object {
        @VisibleForTesting
        val API_KEY_NOT_ADDED = "ADD_YOUR_API_KEY_HERE"

        // does not need to be translated as it's only for the dev
        @VisibleForTesting
        val API_KEY_ERROR_MSG = "You did not add your NEWS API KEY to the project"
    }
}