package agis.guillaume.cleancode.ui.post

import agis.guillaume.cleancode.base.UiSingleEvent
import agis.guillaume.cleancode.base.UiState
import agis.guillaume.cleancode.base.UserEvent
import agis.guillaume.cleancode.model.Post

class PostsListContract {

    // Events that user performed
    sealed class Event : UserEvent {
        data class PostClicked(val post : Post) : Event()
        object ReloadButtonClicked : Event()

    }

    data class State(val posts: List<Post> = listOf(), val isLoading: Boolean = false) : UiState

    // single event displayed on the screen
    sealed class SingleEvent : UiSingleEvent {
        data class DisplayErrorPopup(val message : String?= null) : SingleEvent()
        object DisplayInternetLostMessage : SingleEvent()
        data class DisplayPostDetail(val postSelected: Post) : SingleEvent()
    }
}