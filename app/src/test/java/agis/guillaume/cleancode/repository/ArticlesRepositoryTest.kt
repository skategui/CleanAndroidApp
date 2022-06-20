package agis.guillaume.cleancode.repository

import agis.guillaume.cleancode.api.model.ArticleResponse
import agis.guillaume.cleancode.api.model.ArticlesResponse
import agis.guillaume.cleancode.api.model.SourceResponse
import agis.guillaume.cleancode.api.model.toArticle
import agis.guillaume.cleancode.api.services.ArticlesService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.util.*


internal class ArticlesRepositoryTest {

    @MockK
    private lateinit var service: ArticlesService

    private lateinit var repo : ArticlesRepository

    private val apiKey = "my_key"

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        repo = ArticlesRepository(service)


    }

    @Test
    fun `When fetch valid business articles then get full list`() = runTest  {

        val validArticles = listOf(
            ArticleResponse(
                source = SourceResponse("TechCrunch"),
                author = "Guillaume Agis",
                title = "Another brutal week for crypto and crypto companies",
                description = "test good idea of what went down this week.",
                url = "https://techcrunch.com/2022/06/18/another-brutal-week-for-crypto-and-crypto-companies/",
                urlToImage = "url2",
                publishedAt = Date()
            ),
            ArticleResponse(
                source = SourceResponse("Forbes"),
                author = "Elon Musk",
                title = "Elon announces Tesla will accept DogeCoin ",
                description = "Desc",
                url = "url",
                urlToImage = "image_url",
                publishedAt = Date()
            )
        )
        coEvery { service.getBusinessArticles(apiKey) } returns Response.success(ArticlesResponse(validArticles))

        val expected = validArticles.map { it.toArticle() }

        assertEquals(expected, repo.getBusinessArticles(apiKey))
    }

    @Test
    fun `When fetch valid and invalid articles then keep only valid articles`() = runTest  {

        val validArticle =  ArticleResponse(
            source = SourceResponse("TechCrunch"),
            author = "Guillaume Agis",
            title = "Another brutal week for crypto and crypto companies",
            description = "test good idea of what went down this week.",
            url = "https://techcrunch.com/2022/06/18/another-brutal-week-for-crypto-and-crypto-companies/",
            urlToImage = "url2",
            publishedAt = Date()
        )
        val articles = listOf(validArticle,
            // missing author
            ArticleResponse(
                source = SourceResponse("Forbes"),
                title = "Elon announces Tesla will accept DogeCoin ",
                description = "Desc",
                url = "url",
                urlToImage = "image_url",
                publishedAt = Date()
            ),
            // missing desc
            ArticleResponse(
                source = SourceResponse("Forbes"),
                author = "Elon Musk",
                title = "Elon announces Tesla will accept DogeCoin ",
                url = "url",
                urlToImage = "image_url",
                publishedAt = Date()
            ),
            // missing urlToImage
            ArticleResponse(
                source = SourceResponse("Forbes"),
                author = "Elon Musk",
                title = "Elon announces Tesla will accept DogeCoin ",
                url = "url",
                description = "desc",
                publishedAt = Date()
            )
        )
        coEvery { service.getBusinessArticles(apiKey) } returns Response.success(ArticlesResponse(articles))

        val expected = validArticle.toArticle()

        assertEquals(listOf(expected), repo.getBusinessArticles(apiKey))
    }

}