package agis.guillaume.cleancode.ui.post

import agis.guillaume.cleancode.api.utils.HttpErrorUtils
import agis.guillaume.cleancode.api.utils.doIfFailure
import agis.guillaume.cleancode.api.utils.doIfSuccess
import agis.guillaume.cleancode.base.BaseViewModel
import agis.guillaume.cleancode.tracker.Tracker
import agis.guillaume.cleancode.usecases.PostsUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * PostsListViewModel is responsible to handle the interaction between the user case(handling the business logic)
 * and what's happening on the UI side (display latest state, handle user interactions etc....)
 */
class PostsListViewModel(
    private val postsUseCase: PostsUseCase,
    private val postReducer: PostReducer,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO // its a good practice to have the dispatcher as a param, as it's also useful for the unit test
) :
    BaseViewModel<PostsListContract.Event, PostsListContract.State, PostsListContract.SingleEvent>() {

    init { fetchPosts() }

    /**
     * Fetch the latest posts and reduce the result with the latest state
     */
    private fun fetchPosts() {
        viewModelScope.launch(ioDispatcher) {
            postsUseCase.getPosts()
                .onStart { setState { postReducer.reduce(this, PostReducer.PartialState.DisplayLoader )} }
                .collect { res ->
                    res.doIfFailure { error, throwable ->
                        setState { postReducer.reduce(this, PostReducer.PartialState.HideLoader ) }
                        handleErrors(error, throwable) }
                    res.doIfSuccess { setState { postReducer.reduce(this, PostReducer.PartialState.DisplayPosts(it) ) } }
                }
        }
    }

    /**
     * Handle errors thrown while fetching the articles , emit the event associated and track the error
     *  @param error error message, is exist. Null if not existing
     *  @param throwable error thrown. Null if not existing
     */
    private fun handleErrors(error: String?, throwable: Throwable?) {
        if (HttpErrorUtils.hasLostInternet(throwable))
            setSingleEvent { PostsListContract.SingleEvent.DisplayInternetLostMessage }
        else
            setSingleEvent { PostsListContract.SingleEvent.DisplayErrorPopup(error) }
        Tracker.trackError(throwable)
    }

    override fun createInitialState() = PostsListContract.State()

    /**
     * EVent received from the user interaction, from the UI
     */
    override fun subscribeUserInteraction(event: PostsListContract.Event) {
        when (event) {
            is PostsListContract.Event.PostClicked -> setSingleEvent {
                PostsListContract.SingleEvent.DisplayPostDetail(event.post)
            }
            is PostsListContract.Event.ReloadButtonClicked -> fetchPosts()
        }
    }
}