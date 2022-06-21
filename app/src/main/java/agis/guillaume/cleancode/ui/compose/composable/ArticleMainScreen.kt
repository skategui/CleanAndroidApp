package agis.guillaume.cleancode.ui.compose.composable

import agis.guillaume.cleancode.ui.article.ArticlesListContract
import agis.guillaume.cleancode.ui.article.ArticlesListViewModel
import androidx.compose.runtime.*

@Composable
fun ArticleMainScreen(
    viewModel: ArticlesListViewModel
) {
    val showInternetDialog = remember { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf<String?>(null) }


    // State
    val state by viewModel.uiState.collectAsState()

    when {
        // popup can be displayed above existing content, so its part of another "when"
        state.errorMsgToShow != null -> showErrorDialog.value = state.errorMsgToShow
        state.hasLostInternet -> showInternetDialog.value = true
    }

    when {
        state.isLoading -> LoadingStateContent()
        state.articles.isNotEmpty() && !state.isLoading -> displayListArticlesContent(state.articles) { article ->
            viewModel.setEvent(ArticlesListContract.Interaction.ArticleClicked(article)
            )
        }
        state.articles.isEmpty() && !state.isLoading -> displayEmptyArticleContent()
    }

    // Dialog
    displayErrorDialog(showErrorDialog)
    displayNoInternetDialog(showInternetDialog) {
        viewModel.setEvent(ArticlesListContract.Interaction.ReloadButtonClicked)
    }
}