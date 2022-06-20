package agis.guillaume.cleancode.usecases

import agis.guillaume.cleancode.api.utils.ResultOf
import agis.guillaume.cleancode.datastore.user.UserDatastore
import agis.guillaume.cleancode.model.User
import agis.guillaume.cleancode.repository.IUsersRepository

/**
 * UsersUseCase is responsible to manage all the business logic related to the users
 * No UI logic will be found here
 **/

class UsersUseCase(
    private val usersRepository: IUsersRepository,
    private val userDatastore: UserDatastore
) {

    /**
     * Store the info of the user in the local DB
     *  @param user User to store
     */
    suspend fun saveUser(user : User) {
        userDatastore.saveUser(user)
    }

    /**
     * Get the user from a user ID, from the server if not already fetched, or locally from the datastore
     *  @param userId id of the user to load
     *  @return [ResultOf] [User] user found, or an error contained inside ResultOf if user not found
     */
    suspend fun getUserById(userId: Int): User? {
        val userFound = userDatastore.getUserByID(userId)
        return userFound?.run { userFound
        } ?: usersRepository.getUserById(userId = userId)
    }
}