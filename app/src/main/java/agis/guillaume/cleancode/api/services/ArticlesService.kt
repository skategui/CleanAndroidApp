package agis.guillaume.cleancode.api.services

import agis.guillaume.cleancode.api.model.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Service associated to get the list of articles from the news API
 */
interface ArticlesService {

    @GET("top-headlines")
    suspend fun getBusinessArticles(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String = "fr",
        @Query("category") category: String = "technology"
    ): Response<ArticlesResponse>
}