package agis.guillaume.cleancode.api.services

import agis.guillaume.cleancode.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Service associated to get the author from the post
 */
interface UserService {

    @GET("users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): Response<User>
}