package agis.guillaume.cleancode.api.services

import agis.guillaume.cleancode.api.model.PostResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Service associated to the request to get the "lorem ipsum" of posts
 */
interface PostService {

    @GET("posts")
    suspend fun getPosts(): Response<List<PostResponse>>
}