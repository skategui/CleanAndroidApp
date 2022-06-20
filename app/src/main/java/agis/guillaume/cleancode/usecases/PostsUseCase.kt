package agis.guillaume.cleancode.usecases

import agis.guillaume.cleancode.api.utils.ResultOf
import agis.guillaume.cleancode.datastore.post.PostsDatastore
import agis.guillaume.cleancode.model.Post
import agis.guillaume.cleancode.repository.IPostsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * PostsUseCase is responsible to manage all the business logic related to the Post
 * such as associated a user to a loaded post (in this example. No UI logic will be found here)
 */
class PostsUseCase(
    private val usersUseCase: UsersUseCase,
    private val postsRepository: IPostsRepository,
    private val postsDatastore: PostsDatastore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO, // its a good practice to have the dispatcher as a param, as it's also useful for the unit tests
    private val delayInMs: Long = 4000
) {
    /**
     * Get the posts list from the server
     * @return [Flow] [List] [ResultOf] [Post]  list of posts fetched from server
     */
    suspend fun getPosts(): Flow<ResultOf<List<Post>>> {
        return flow {
            try {
                val postsStored = postsDatastore.getPosts()
                if (postsStored.isNotEmpty())
                    emit(ResultOf.Success(postsStored))
                else
                    delay(delayInMs) // add a small delay to see the loading progress if not data stored
                val posts = postsRepository.getPosts()
                    .map { post ->

                        val user = usersUseCase.getUserById(post.userId)
                        if (user != null) {
                            usersUseCase.saveUser(user)
                        }
                        Post(
                            id = post.id,
                            user = user,
                            title = post.title,
                            body = post.body
                        )
                    }
                postsDatastore.savePosts(posts)
                emit(ResultOf.Success(posts))
            } catch (e: Exception) {
                emit(ResultOf.Failure(exception = e))
            }
        }.flowOn(ioDispatcher)
    }
}