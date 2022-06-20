package agis.guillaume.cleancode.ui.post

import agis.guillaume.cleancode.model.Post
import agis.guillaume.cleancode.model.User
import org.junit.Assert
import org.junit.Test


internal class PostReducerTest {

    private val reducer = PostReducer()

    private val user = User(
        id = 32,
        website = "http://www.hire-me-maybe.com",
        username = "skategui",
        email = "g@gmail.com",
        phone = "21312-12301-2313"
    )

    private val posts = listOf(
        Post(
            user = user,
            id = 43,
            title = "Another brutal week for crypto and crypto companies",
            body = "test good idea of what went down this week."
        ),
        Post(
            user = user,
            id = 12,
            title = "Elon announces Tesla will accept DogeCoin ",
            body = "Desc"
        )
    )

    @Test
    fun `Loading is true with DisplayLoader partial state is emit`() {

        val state = PostsListContract.State(posts = posts, isLoading = false)
        Assert.assertEquals(
            state.copy(isLoading = true), reducer.reduce(
                state,
                PostReducer.PartialState.DisplayLoader
            )
        )
    }

    @Test
    fun `Loading is false with HideLoader partial state is emit`() {

        val state = PostsListContract.State(posts = posts, isLoading = true)
        Assert.assertEquals(
            state.copy(isLoading = false), reducer.reduce(
                state,
                PostReducer.PartialState.HideLoader
            )
        )
    }

    @Test
    fun `Set posts and put loading false when DisplayPosts is emit`() {

        val state = PostsListContract.State(posts = emptyList(), isLoading = true)
        Assert.assertEquals(
            state.copy(isLoading = false, posts = posts), reducer.reduce(
                state,
                PostReducer.PartialState.DisplayPosts(posts)
            )
        )

    }
}