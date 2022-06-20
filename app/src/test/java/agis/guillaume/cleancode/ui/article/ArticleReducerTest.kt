package agis.guillaume.cleancode.ui.article

import agis.guillaume.cleancode.model.Article
import org.junit.Assert
import org.junit.Test
import java.util.*

internal class ArticleReducerTest {
    private val reducer = ArticleReducer()

    private val articles = listOf(
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

    @Test
    fun `Loading is true with DisplayLoader partial state is emit`() {

        val state = ArticlesListContract.State(articles = articles, isLoading = false)
        Assert.assertEquals(
            state.copy(isLoading = true), reducer.reduce(
                state,
                ArticleReducer.PartialState.DisplayLoader
            )
        )
    }

    @Test
    fun `Loading is false with HideLoader partial state is emit`() {

        val state = ArticlesListContract.State(articles = articles, isLoading = true)
        Assert.assertEquals(
            state.copy(isLoading = false), reducer.reduce(
                state,
                ArticleReducer.PartialState.HideLoader
            )
        )
    }

    @Test
    fun `Set articles and put loading false when DisplayArticles is emit`() {

        val state = ArticlesListContract.State(articles = emptyList(), isLoading = true)
        Assert.assertEquals(
            state.copy(isLoading = false, articles = articles), reducer.reduce(
                state,
                ArticleReducer.PartialState.DisplayArticles(articles)
            )
        )

    }
}