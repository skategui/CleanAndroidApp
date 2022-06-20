package agis.guillaume.cleancode.ui.article

import agis.guillaume.cleancode.api.utils.ResultOf
import agis.guillaume.cleancode.model.Article
import agis.guillaume.cleancode.usecases.ArticlesUseCase
import agis.guillaume.cleancode.utils.MainCoroutineRule
import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import java.net.UnknownHostException
import java.util.*

internal class ArticlesListViewModelTest {

    @MockK
    private lateinit var usecase: ArticlesUseCase

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val reducer = ArticleReducer()

    private lateinit var viewModel: ArticlesListViewModel

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

    }

    private fun createViewModel() {
        viewModel = ArticlesListViewModel(usecase, reducer, dispatcher)
    }

    @Test
    fun `When loading articles is a success then emit Success`() = runTest {

        coEvery { usecase.loadArticles() } returns flowOf(ResultOf.Success(Unit))
        coEvery { usecase.getLoadedArticles() } returns flowOf(emptyList())

        createViewModel()

        viewModel.uiState.test {
            Assert.assertEquals(
                ArticlesListContract.State(isLoading = false, articles = emptyList()), awaitItem())
        }
    }

    @Test
    fun `When loading article is a failure due to internet issue then display lost internet message`() =
        runTest {

            coEvery { usecase.loadArticles() } returns flowOf(ResultOf.Failure(exception = UnknownHostException()))
            coEvery { usecase.getLoadedArticles() } returns flowOf(emptyList())

            createViewModel()

            viewModel.uiState.test {
                Assert.assertEquals(
                    ArticlesListContract.State(isLoading = false, articles = emptyList()), awaitItem())
            }
            viewModel.singleEvent.test {
                val event = awaitItem()
                Assert.assertEquals(
                    ArticlesListContract.SingleEvent.DisplayInternetLostMessage,
                    event
                )
            }
        }

    @Test
    fun `When loading article is a failure due to unknown error then display popup with message`() =
        runTest {

            val errorMsg = "not found"
            coEvery { usecase.loadArticles() } returns flowOf(
                ResultOf.Failure(
                    message = errorMsg,
                    exception = Exception("test")
                )
            )
            coEvery { usecase.getLoadedArticles() } returns flowOf(emptyList())

            createViewModel()

            viewModel.uiState.test {
                Assert.assertEquals(
                    ArticlesListContract.State(isLoading = false, articles = emptyList()), awaitItem())
            }

            viewModel.singleEvent.test {
                val event = awaitItem()
                Assert.assertEquals(
                    ArticlesListContract.SingleEvent.DisplayErrorPopup(errorMsg),
                    event
                )
            }
        }


    @Test
    fun `When articles are emitted then display the list of articles`() = runTest {

        val articles = listOf(
            Article(
                source = "TechCrunch",
                author = "Guillaume Agis",
                title = "Another brutal week for crypto and crypto companies",
                description = "test good idea of what went down this week.",
                url = "https://techcrunch.com/2022/06/18/another-brutal-week-for-crypto-and-crypto-companies/",
                urlToImage = "url2",
                publishedAt = Date()
            ),
            Article(
                source = "Forbes",
                author = "Elon Musk",
                title = "Elon announces Tesla will accept DogeCoin ",
                description = "Desc",
                url = "url",
                urlToImage = "image_url",
                publishedAt = Date()
            )
        )

        coEvery { usecase.loadArticles() } returns flowOf(ResultOf.Success(Unit))
        coEvery { usecase.getLoadedArticles() } returns flowOf(articles)

        createViewModel()

        viewModel.uiState.test {
            Assert.assertEquals(
                ArticlesListContract.State(isLoading = false, articles = articles), awaitItem())
        }
    }

    @Test
    fun `When user click on article then open article`() = runTest {


        coEvery { usecase.loadArticles() } returns flowOf(ResultOf.Success(Unit))
        coEvery { usecase.getLoadedArticles() } returns flowOf(emptyList())

        val article = Article(
            source = "Forbes",
            author = "Elon Musk",
            title = "Elon announces Tesla will accept DogeCoin ",
            description = "Desc",
            url = "url",
            urlToImage = "image_url",
            publishedAt = Date()
        )

        createViewModel()

        viewModel.setEvent(ArticlesListContract.Event.ArticleClicked(article))

        viewModel.singleEvent.test {
            val state = awaitItem()
            Assert.assertEquals(state, ArticlesListContract.SingleEvent.OpenArticle(article.url))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `When user click reload button then fetch articles`() = runTest {

        coEvery { usecase.loadArticles() } returns flowOf(ResultOf.Success(Unit))
        coEvery { usecase.getLoadedArticles() } returns flowOf(emptyList())

        createViewModel()

        viewModel.setEvent(ArticlesListContract.Event.ReloadButtonClicked)

        coVerify(exactly = 2) {
            usecase.loadArticles()
        }
    }
}