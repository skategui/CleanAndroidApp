package agis.guillaume.cleancode.ui.post

import agis.guillaume.cleancode.api.utils.HttpErrorUtils
import agis.guillaume.cleancode.api.utils.doIfFailure
import agis.guillaume.cleancode.api.utils.doIfSuccess
import agis.guillaume.cleancode.base.BaseViewModel
import agis.guillaume.cleancode.tracker.Tracker
import agis.guillaume.cleancode.usecases.PostsUseCase
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * PostsListViewModel is responsible to handle the interaction between the user case(handling the business logic)
 * and what's happening on the UI side (display latest state, handle user interactions etc....)
 *
 * This viewmodel will simply load the data from the remote server and emit them in a flow, that will collect
 * them and display them on the view
 */
class PostsListViewModel(
    private val postsUseCase: PostsUseCase,
    private val postReducer: PostReducer,
    private val delayInMs : Long = 2000,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO // its a good practice to have the dispatcher as a param, as it's also useful for the unit test
) :
    BaseViewModel<PostsListContract.Interaction, PostsListContract.State, PostsListContract.SingleEvent>(PostsListContract.State()) {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        fetchPosts()
    }

    /**
     * Fetch the latest posts and reduce the result with the latest state
     */
    private fun fetchPosts() {
        viewModelScope.launch(ioDispatcher) {
            postsUseCase.getPosts()
                .onStart {
                    setState { postReducer.reduce(this, PostReducer.PartialState.DisplayLoader )}
                    delay(delayInMs) // so we can see the cute little animation. Only for this app test
                }
                .collect { res ->
                    setState { postReducer.reduce(this, PostReducer.PartialState.HideLoader ) }
                    res.doIfFailure { _, throwable -> handleErrors(throwable) }
                    res.doIfSuccess { setState { postReducer.reduce(this, PostReducer.PartialState.DisplayPosts(it) ) } }
                }
        }
    }

    /**
     * Handle errors thrown while fetching the articles , emit the event associated and track the error
     *  @param throwable error thrown. Null if not existing
     */
    private fun handleErrors( throwable: Throwable?) {
        if (HttpErrorUtils.hasLostInternet(throwable))
            setState { postReducer.reduce(this, PostReducer.PartialState.DisplayInternetLostMsg ) }
        else
            setState { postReducer.reduce(this, PostReducer.PartialState.DisplayErrorMsg ) }
        Tracker.trackError(throwable)
    }

    /**
     * Event received from the user interaction, from the UI
     */
    override fun subscribeUserInteraction(event: PostsListContract.Interaction) {
        when (event) {
            is PostsListContract.Interaction.PostClicked -> setSingleEvent {
                PostsListContract.SingleEvent.DisplayPostDetail(event.post)
            }
            is PostsListContract.Interaction.ReloadButtonClicked -> fetchPosts()
        }
    }
}