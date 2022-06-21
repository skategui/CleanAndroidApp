package agis.guillaume.cleancode.ui.post

import agis.guillaume.cleancode.model.Post

/**
 * PostReducer is responsible to build a new State according to the current state and the Partial state, the state that will be made
 * Modification of the state only happen here
 */
class PostReducer {

    /**
     * Reduce a state given the current state and a partial state
     *  @param state current state
     *  @param partialState state to be created
     *  @return New created state
     */
    fun reduce(
        state: PostsListContract.State,
        partialState: PartialState
    ): PostsListContract.State {
        return when (partialState) {
            is PartialState.DisplayLoader -> state.copy(
                isLoading = true,
                hasErrorMsgToShow = false,
                hasLostInternet = false
            )
            is PartialState.DisplayPosts -> state.copy(
                posts = partialState.posts,
                isLoading = false,
                hasErrorMsgToShow = false,
                hasLostInternet = false
            )
            PartialState.HideLoader -> state.copy(
                isLoading = false,
                hasErrorMsgToShow = false,
                hasLostInternet = false
            )
            PartialState.DisplayErrorMsg -> state.copy(hasErrorMsgToShow = true, isLoading = false)
            PartialState.DisplayInternetLostMsg -> state.copy(
                hasLostInternet = true,
                isLoading = false
            )
        }
    }

    sealed class PartialState {
        object DisplayErrorMsg : PartialState()
        object DisplayInternetLostMsg : PartialState()
        object DisplayLoader : PartialState()
        object HideLoader : PartialState()
        data class DisplayPosts(val posts: List<Post>) : PartialState()
    }
}
