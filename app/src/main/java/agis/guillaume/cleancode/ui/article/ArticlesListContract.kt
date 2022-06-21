package agis.guillaume.cleancode.ui.article

import agis.guillaume.cleancode.base.UiSingleEvent
import agis.guillaume.cleancode.base.UiState
import agis.guillaume.cleancode.base.UserInteraction
import agis.guillaume.cleancode.model.Article

class ArticlesListContract {

    // Events that user performed
    sealed class Interaction : UserInteraction {
        data class ArticleClicked(val article: Article) : Interaction()
        object ReloadButtonClicked : Interaction()
    }

    // current UI State
    data class State(
        val articles: List<Article> = listOf(),
        val isLoading: Boolean = false,
        val errorMsgToShow: String? = null,
        val hasLostInternet: Boolean = false
    ) : UiState

    // single event to be made on the screen
    sealed class SingleEvent : UiSingleEvent {
        data class OpenArticle(val url: String) : SingleEvent()
    }
}