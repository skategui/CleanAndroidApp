package agis.guillaume.cleancode.repository

import agis.guillaume.cleancode.api.model.*
import agis.guillaume.cleancode.api.services.PostService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

internal class PostsRepositoryTest {

    @MockK
    private lateinit var service :PostService

    private lateinit var repo : PostsRepository

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        repo = PostsRepository(service)
    }

    @Test
    fun `When fetch posts then get full list`() = runTest  {

        val validPosts = listOf(
            PostResponse(
                id = 31,
                userId = 42,
                title = "Another brutal week for crypto and crypto companies",
                body = "test good idea of what went down this week."
            ),
            PostResponse(
                id = 32,
                userId = 40,
                title = "Elon announces Tesla will accept DogeCoin ",
                body = "Desc",
            )
        )
        coEvery { service.getPosts() } returns Response.success(validPosts)

        Assert.assertEquals(validPosts, repo.getPosts())
    }
}