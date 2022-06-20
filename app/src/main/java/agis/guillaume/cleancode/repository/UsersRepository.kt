package agis.guillaume.cleancode.repository

import agis.guillaume.cleancode.api.services.UserService
import agis.guillaume.cleancode.model.User

/**
 * UsersRepository is responsible to make the HTTP request to the server in order to load the user
 * (using retrofit)
 */
class UsersRepository(private val service: UserService) : IUsersRepository {

    /**
     *  Get the user from a user ID, from the server
     *  @param userId id of the user to load
     *  @return [User?] user, if exist, null otherwise
     */
    override suspend fun getUserById(userId: Int) = service.getUserById(userId).body()
}

/**
 * IUsersRepository is is an interface to hide the implementation of the repository.
 * PS : it's a bit over engineered for this interface, but useful for the unit tests :)
 */
interface IUsersRepository {
    suspend fun getUserById(userId: Int): User?
}