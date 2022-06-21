package agis.guillaume.cleancode.ui.compose.composable

import agis.guillaume.cleancode.ui.article.ArticlesListContract
import agis.guillaume.cleancode.ui.article.ArticlesListViewModel
import android.util.Log
import androidx.compose.runtime.*

@Composable
fun ArticleMainScreen(
    viewModel: ArticlesListViewModel,
    openArticle: (url: String) -> Unit
) {

    val showInternetDialog = remember { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf<String?>(null) }


    // State
    val state by viewModel.uiState.collectAsState()
    //Log.e("ARTICLE", "NEW state RECEIVED $state")
    when {
        state.isLoading -> LoadingStateContent()
        state.articles.isNotEmpty() && !state.isLoading -> displayListArticlesContent(state.articles) { article ->
            viewModel.setEvent(
                ArticlesListContract.Interaction.ArticleClicked(article)
            )
        }
        state.articles.isEmpty() && !state.isLoading -> displayEmptyArticleContent()
    }

    // Single event
    val singleEvent by viewModel.singleEvent.collectAsState(initial = null)
    Log.e("EVENT", "NEW event detected $singleEvent" )


    when (singleEvent) {
        is ArticlesListContract.SingleEvent.DisplayErrorPopup -> showErrorDialog.value = (singleEvent as ArticlesListContract.SingleEvent.DisplayErrorPopup).message
        is ArticlesListContract.SingleEvent.DisplayInternetLostPopup -> showInternetDialog.value = true
        is ArticlesListContract.SingleEvent.OpenArticle -> openArticle((singleEvent as ArticlesListContract.SingleEvent.OpenArticle).url)
        null -> {}
    }


    // Dialog
    displayErrorDialog(showErrorDialog)
    displayNoInternetDialog(showInternetDialog) { viewModel.setEvent(ArticlesListContract.Interaction.ReloadButtonClicked) }
}