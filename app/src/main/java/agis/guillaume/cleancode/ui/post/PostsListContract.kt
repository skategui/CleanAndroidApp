package agis.guillaume.cleancode.ui.post

import agis.guillaume.cleancode.base.UiSingleEvent
import agis.guillaume.cleancode.base.UiState
import agis.guillaume.cleancode.base.UserInteraction
import agis.guillaume.cleancode.model.Post

class PostsListContract {

    // Interaction that the user performed
    sealed class Interaction : UserInteraction {
        data class PostClicked(val post: Post) : Interaction()
        object ReloadButtonClicked : Interaction()

    }

    // current state of the UI
    data class State(
        val posts: List<Post> = listOf(),
        val isLoading: Boolean = false,
        val hasErrorMsgToShow: Boolean = false,
        val hasLostInternet: Boolean = false
    ) : UiState

    // single event to display on the screen
    sealed class SingleEvent : UiSingleEvent {
        data class DisplayPostDetail(val postSelected: Post) : SingleEvent()
    }
}