package agis.guillaume.cleancode.ui.post

import agis.guillaume.cleancode.api.utils.ResultOf
import agis.guillaume.cleancode.model.Post
import agis.guillaume.cleancode.model.User
import agis.guillaume.cleancode.usecases.PostsUseCase
import agis.guillaume.cleancode.utils.MainCoroutineRule
import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

internal class PostsListViewModelTest {

    @MockK
    private lateinit var usecase: PostsUseCase

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val reducer = PostReducer()

    private lateinit var viewModel: PostsListViewModel

    private val dispatcher = TestCoroutineDispatcher()

    private val user = User(
        id = 32,
        website = "http://www.hire-me-maybe.com",
        username = "skategui",
        email = "g@gmail.com",
        phone = "21312-12301-2313"
    )

    private val posts = listOf(
        Post(
            id = 31,
            user = user,
            title = "Another brutal week for crypto and crypto companies",
            body = "test good idea of what went down this week."
        ),
        Post(
            id = 32,
            user = user,
            title = "Elon announces Tesla will accept DogeCoin ",
            body = "Desc",
        )
    )


    @Before
    fun setup() {
        MockKAnnotations.init(this)

    }

    private fun createViewModel() {
        viewModel = PostsListViewModel(usecase, reducer, dispatcher)
    }

    @Test
    fun `When loading posts is a failure due to unknown error then display popup with message`() =
        runTest {

            val errorMsg = "not found"
            coEvery { usecase.getPosts() } returns flowOf(
                ResultOf.Failure(
                    message = errorMsg,
                    exception = Exception("test")
                )
            )

            createViewModel()

            viewModel.uiState.test {
                Assert.assertEquals(
                    PostsListContract.State(isLoading = false, posts = emptyList()), awaitItem()
                )
            }

            viewModel.singleEvent.test {
                val event = awaitItem()
                Assert.assertEquals(
                    PostsListContract.SingleEvent.DisplayErrorPopup(errorMsg),
                    event
                )
            }
        }


    @Test
    fun `When posts are emitted then display the list of posts`() = runTest {

        coEvery { usecase.getPosts() } returns flowOf(ResultOf.Success(posts))

        createViewModel()

        viewModel.uiState.test {
            Assert.assertEquals(
                PostsListContract.State(isLoading = false, posts = posts), awaitItem()
            )
        }
    }

    @Test
    fun `When user click reload button then fetch posts`() = runTest {

        coEvery { usecase.getPosts() } returns flowOf(ResultOf.Success(posts))

        createViewModel()

        viewModel.setEvent(PostsListContract.Event.ReloadButtonClicked)

        coVerify(exactly = 2) {
            usecase.getPosts()
        }
    }

    @Test
    fun `When user click on post then open post`() = runTest {

        coEvery { usecase.getPosts() } returns flowOf(ResultOf.Success(posts))


        createViewModel()

        val post = posts.first()

        viewModel.setEvent(PostsListContract.Event.PostClicked(post))

        viewModel.singleEvent.test {
            val state = awaitItem()
            Assert.assertEquals(state, PostsListContract.SingleEvent.DisplayPostDetail(post))
            cancelAndIgnoreRemainingEvents()
        }
    }
}