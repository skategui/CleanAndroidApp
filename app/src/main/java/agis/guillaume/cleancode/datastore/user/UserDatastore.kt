package agis.guillaume.cleancode.datastore.user

import agis.guillaume.cleancode.model.User

/**
 * UserLocalDatastore is responsible to store the users into the local DB (using android Room)
 */
internal class UserLocalDatastore(private val userDao: UserDao) : UserDatastore {

    /**
     * Save user in the DB
     *  @param user user to store
     */
    override suspend fun saveUser(user: User) {
        userDao.insertUser(user.toUserEntity())
    }

    /**
     * Get user by ID
     *  @param userId id of the user to fetch
     *  @return [User]  user found, null otherwise
     */
    override suspend fun getUserByID(userId: Int) = userDao.getUserByID(userId)?.toUser()
}

/**
 * UserDatastore is is an interface to hide the implementation of the datastore.
 * PS : it's a bit over engineered for this app, but useful for the unit tests :)
 */
interface UserDatastore {
    suspend fun saveUser(user: User)
    suspend fun getUserByID(userId: Int): User?
}
