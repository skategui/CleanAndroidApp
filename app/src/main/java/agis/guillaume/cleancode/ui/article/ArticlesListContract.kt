package agis.guillaume.cleancode.ui.article

import agis.guillaume.cleancode.base.UiSingleEvent
import agis.guillaume.cleancode.base.UiState
import agis.guillaume.cleancode.base.UserEvent
import agis.guillaume.cleancode.model.Article
import agis.guillaume.cleancode.model.Post

class ArticlesListContract {

    // Events that user performed
    sealed class Event : UserEvent {
        data class ArticleClicked(val article: Article) : Event()
        object ReloadButtonClicked : Event()
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