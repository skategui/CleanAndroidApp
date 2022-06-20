package agis.guillaume.cleancode.repository

import agis.guillaume.cleancode.api.services.UserService
import agis.guillaume.cleancode.model.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response


internal class UsersRepositoryTest{

    @MockK
    private lateinit var service : UserService

    private lateinit var repo : UsersRepository

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        repo = UsersRepository(service)
    }

    @Test
    fun `When fetch existing user then get user found`() = runTest  {

        val user = User(
            id = 42,
            website = "http://www.hire-me-maybe.com",
            username = "skategui",
            email = "g@gmail.com",
            phone = "21312-12301-2313"
        )
        coEvery { service.getUserById(user.id) } returns Response.success(user)

        Assert.assertEquals(user, repo.getUserById(user.id))
    }

    @Test
    fun `When fetch not existing user then get null`() = runTest  {

        val user = User(
            id = 42,
            website = "http://www.hire-me-maybe.com",
            username = "skategui",
            email = "g@gmail.com",
            phone = "21312-12301-2313"
        )
        coEvery { service.getUserById(user.id) } returns Response.success(user)
        coEvery { service.getUserById(user.id + 1) } returns Response.error(400, ResponseBody.create(null, "" ))

        Assert.assertEquals(null, repo.getUserById(user.id + 1))
    }
}