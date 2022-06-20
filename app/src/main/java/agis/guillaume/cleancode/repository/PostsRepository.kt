package agis.guillaume.cleancode.repository

import agis.guillaume.cleancode.api.model.PostResponse
import agis.guillaume.cleancode.api.services.PostService

/**
 * PostsRepository is responsible to make the HTTP request to the server in order to load the posts
 * (using retrofit)
 */
class PostsRepository(private val service: PostService) : IPostsRepository {

    /**
     * Get the list of posts  from the server
     *  @return  [List] [PostResponse] Response from the server, containing the list of posts if success
     */
    override suspend fun getPosts() = service.getPosts().body()!!
}

/**
 * IPostsRepository is is an interface to hide the implementation of the repository.
 * PS : it's a bit over engineered for this interface, but useful for the unit tests :)
 */
interface IPostsRepository {
    suspend fun getPosts(): List<PostResponse>
}