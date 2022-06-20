package agis.guillaume.cleancode.usecases

import agis.guillaume.cleancode.api.model.PostResponse
import agis.guillaume.cleancode.api.utils.ResultOf
import agis.guillaume.cleancode.datastore.post.PostsDatastore
import agis.guillaume.cleancode.model.Post
import agis.guillaume.cleancode.model.User
import agis.guillaume.cleancode.repository.IPostsRepository
import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class PostsUseCaseTest {

    @MockK
    private lateinit var repo: IPostsRepository

    @MockK
    private lateinit var datastore: PostsDatastore

    @MockK
    private lateinit var userUsecase: UsersUseCase

    private lateinit var usecase: PostsUseCase

    private val validPosts = listOf(
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
        usecase = PostsUseCase(
            usersUseCase = userUsecase,
            postsRepository = repo,
            postsDatastore = datastore,
            delayInMs = 1
        )
    }

    @Test
    fun `When loading posts then emit those posts and store them locally`() = runTest {

        coEvery { datastore.getPosts() } returns emptyList()
        coEvery { repo.getPosts() } returns validPosts
        coEvery { userUsecase.getUserById(any()) } returns user
        coEvery { userUsecase.saveUser(user) } returns Unit
        coEvery { datastore.savePosts(any()) } returns Unit

        val expected = validPosts.map { post ->
            Post(
                id = post.id,
                user = user,
                title = post.title,
                body = post.body
            )
        }

        usecase.getPosts().test {
            val res = awaitItem()
            Assert.assertTrue(res is ResultOf.Success<List<Post>>)
            val postsFetched = (res as ResultOf.Success<List<Post>>).value
            Assert.assertEquals(expected, postsFetched)
            awaitComplete()
        }



        coVerify {
            userUsecase.saveUser(user)
            datastore.savePosts(expected)
        }
    }

    @Test
    fun `When loading posts and having stored posts then emit the stored posts and then the fetched posts`() =
        runTest {

            val storedPosts = listOf(
                Post(
                    id = 1,
                    user = user,
                    title = "Post title 2",
                    body = "test good idea of what went down this week."
                ),
                Post(
                    id = 3,
                    user = user,
                    title = "Post title",
                    body = "Desc"
                )
            )

            coEvery { datastore.getPosts() } returns storedPosts
            coEvery { repo.getPosts() } returns validPosts
            coEvery { userUsecase.getUserById(any()) } returns user
            coEvery { userUsecase.saveUser(user) } returns Unit
            coEvery { datastore.savePosts(any()) } returns Unit

            val expected = validPosts.map { post ->
                Post(
                    id = post.id,
                    user = user,
                    title = post.title,
                    body = post.body
                )
            }

            usecase.getPosts().test {
                // stored data
                val res = awaitItem()
                Assert.assertTrue(res is ResultOf.Success<List<Post>>)
                val postStored = (res as ResultOf.Success<List<Post>>).value
                Assert.assertEquals(storedPosts, postStored)

                // fetched posts
                val fetchedPosts = awaitItem()
                Assert.assertTrue(fetchedPosts is ResultOf.Success<List<Post>>)
                val postsFetched = (fetchedPosts as ResultOf.Success<List<Post>>).value
                Assert.assertEquals(expected, postsFetched)
                awaitComplete()
            }
        }


    @Test
    fun `When throwing error while loading posts then emit the error`() = runTest {

        val errorMsg = "error Msg"
        coEvery { datastore.getPosts() } returns emptyList()
        coEvery { repo.getPosts() } throws Exception(errorMsg)

        usecase.getPosts().test {
            val res = awaitItem()
            Assert.assertTrue(res is ResultOf.Failure)
            val errorThrown = (res as ResultOf.Failure).exception?.message
            Assert.assertEquals(errorMsg, errorThrown)
            awaitComplete()
        }

        coVerify(exactly = 0) {
            userUsecase.saveUser(user)
            datastore.savePosts(any())
        }
    }
}