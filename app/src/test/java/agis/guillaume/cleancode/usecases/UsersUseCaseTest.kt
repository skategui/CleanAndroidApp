package agis.guillaume.cleancode.usecases

import agis.guillaume.cleancode.datastore.user.UserDatastore
import agis.guillaume.cleancode.model.User
import agis.guillaume.cleancode.repository.IUsersRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test


internal class UsersUseCaseTest {

    @MockK
    private lateinit var repo: IUsersRepository

    @MockK
    private lateinit var datastore: UserDatastore

    private lateinit var usecase: UsersUseCase

    private val user = User(
        id = 32,
        website = "http://www.hire-me-maybe.com",
        username = "skategui",
        email = "g@gmail.com",
        phone = "21312-12301-2313"
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        usecase = UsersUseCase(repo, datastore)
    }

    @Test
    fun `When saving user, then store the user locally`() = runTest {
        coEvery { datastore.saveUser(user) } returns Unit
        usecase.saveUser(user)
        coVerify {
            datastore.saveUser(user)
        }
    }

    @Test
    fun `When user is stored in local DB then do not make request to remote server`() = runTest {
        coEvery { datastore.getUserByID(user.id) } returns user
        val userFound = usecase.getUserById(user.id)

        Assert.assertEquals(user, userFound)
        coVerify(exactly = 0) {
            repo.getUserById(user.id)
        }
    }

    @Test
    fun `When user is not stored in local DB then get the user from the remote server`() = runTest {
        coEvery { datastore.getUserByID(user.id) } returns null
        coEvery { repo.getUserById(user.id) } returns user

        val userFound = usecase.getUserById(user.id)
        Assert.assertEquals(user, userFound)
        coVerify {
            repo.getUserById(user.id)
        }
    }
}