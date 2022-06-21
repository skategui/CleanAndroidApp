package agis.guillaume.cleancode.ui.compose.composable

import agis.guillaume.cleancode.ui.article.ArticlesListContract
import agis.guillaume.cleancode.ui.article.ArticlesListViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ArticleMainScreen(
    viewModel: ArticlesListViewModel,
    openArticle: (url: String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    when {
        state.isLoading -> LoadingStateContent()
        state.articles.isNotEmpty() -> displayListArticlesContent(state.articles) { article ->
            viewModel.setEvent(
                ArticlesListContract.Interaction.ArticleClicked(article)
            )
        }
        state.articles.isEmpty() -> displayEmptyArticleContent()
    }

    val singleEvent by viewModel.singleEvent.collectAsState(initial = null)
    when (singleEvent) {
        is ArticlesListContract.SingleEvent.DisplayErrorPopup -> displayErrorContent((singleEvent as ArticlesListContract.SingleEvent.DisplayErrorPopup).message)
        is ArticlesListContract.SingleEvent.DisplayInternetLostMessage -> displayNoInternetContent()
        is ArticlesListContract.SingleEvent.OpenArticle -> openArticle((singleEvent as ArticlesListContract.SingleEvent.OpenArticle).url)
        null -> {}
    }
}