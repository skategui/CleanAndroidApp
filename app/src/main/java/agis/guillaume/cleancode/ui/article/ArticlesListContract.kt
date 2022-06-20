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

    data class State(val articles: List<Article> = listOf(), val isLoading: Boolean = false) :
        UiState

    // single event displayed on the screen
    sealed class SingleEvent : UiSingleEvent {
        data class DisplayErrorPopup(val message: String? = null) : SingleEvent()
        object DisplayInternetLostMessage : SingleEvent()
        data class OpenArticle(val url: String) : SingleEvent()
    }
}