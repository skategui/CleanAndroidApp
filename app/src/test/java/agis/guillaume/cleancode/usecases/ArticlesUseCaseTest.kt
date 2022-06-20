package agis.guillaume.cleancode.usecases

import agis.guillaume.cleancode.api.utils.ResultOf
import agis.guillaume.cleancode.datastore.article.ArticlesDatastore
import agis.guillaume.cleancode.model.Article
import agis.guillaume.cleancode.repository.IArticlesRepository
import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

internal class ArticlesUseCaseTest {

    @MockK
    private lateinit var repo: IArticlesRepository

    @MockK
    private lateinit var datastore: ArticlesDatastore

    private lateinit var usecase: ArticlesUseCase

    private val apiKey = "my_key"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        usecase = ArticlesUseCase(repo, datastore, apiKey)
        coEvery { datastore.saveArticles(any()) } returns Unit
    }

    @Test
    fun `When loading article is a success then emit Success`() = runTest {

        val validArticles = listOf(
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

        coEvery { repo.getBusinessArticles(apiKey) } returns validArticles
        usecase.loadArticles().test {
            val res = awaitItem()
            Assert.assertTrue(res is ResultOf.Success<Unit>)
            awaitComplete()
        }
    }

    @Test
    fun `When loading article is a failing then emit Failure with the message`() = runTest {
        val errorrMsg = "my error"
        coEvery { repo.getBusinessArticles(apiKey) } throws Exception(errorrMsg)
        usecase.loadArticles().test {
            val res = awaitItem()
            val failureMsg = res as ResultOf.Failure
            Assert.assertEquals(errorrMsg, failureMsg.exception!!.message)
            awaitComplete()
        }
    }

    @Test
    fun `When loading article without updating the API key will throw an error`() = runTest {

        val errorrMsg = "my error"

        val usecase = ArticlesUseCase(repo, datastore, ArticlesUseCase.API_KEY_NOT_ADDED)

        coEvery { repo.getBusinessArticles(apiKey) } throws Exception(errorrMsg)
        usecase.loadArticles().test {
            val res = awaitItem()
            val failureMsg = res as ResultOf.Failure
            Assert.assertEquals(ArticlesUseCase.API_KEY_ERROR_MSG, failureMsg.exception!!.message)
            awaitComplete()
        }
    }


    @Test
    fun `When articles are stored in DB then emit loaded articles from DB`() = runTest {

        val validArticles = listOf(
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

        val articlesInDb: MutableStateFlow<List<Article>> = MutableStateFlow(emptyList())


        coEvery { datastore.getArticles() } returns articlesInDb

        usecase.getLoadedArticles().test {

            // emit empty list
            articlesInDb.emit(emptyList())
            Assert.assertEquals(emptyList<Article>(), awaitItem())

            // emit  list with one item
            articlesInDb.emit(listOf(validArticles.first()))
            Assert.assertEquals(listOf(validArticles.first()), awaitItem())

            // emit  list with two items
            articlesInDb.emit(validArticles)
            Assert.assertEquals(validArticles, awaitItem())
        }
    }
}
