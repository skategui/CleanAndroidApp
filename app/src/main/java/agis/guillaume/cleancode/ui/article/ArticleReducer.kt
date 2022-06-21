package agis.guillaume.cleancode.ui.article

import agis.guillaume.cleancode.model.Article

/**
 * ArticleReducer is responsible to build a new State according to the current state and the Partial state, the state that will be made
 * Modification of the state only happen here
 */
class ArticleReducer {

    /**
     * Reduce a state given the current state and a partial state
     *  @param state current state
     *  @param partialState state to be created
     *  @return New created state
     */
    fun reduce(
        state: ArticlesListContract.State,
        partialState: PartialState
    ): ArticlesListContract.State {
        return when (partialState) {
            is PartialState.DisplayLoader -> state.copy(
                isLoading = true,
                errorMsgToShow = null,
                hasLostInternet = false
            )
            is PartialState.DisplayArticles -> state.copy(
                articles = partialState.articles,
                isLoading = false
            )
            PartialState.HideLoader -> state.copy(
                isLoading = false,
                errorMsgToShow = null,
                hasLostInternet = false
            )
            is PartialState.DisplayErrorMsg -> state.copy(
                errorMsgToShow = partialState.msg,
                isLoading = false
            )
            PartialState.DisplayInternetLostMsg -> state.copy(
                hasLostInternet = true,
                isLoading = false
            )
        }
    }

    sealed class PartialState {
        data class DisplayErrorMsg(val msg: String?) : PartialState()
        object DisplayInternetLostMsg : PartialState()
        object DisplayLoader : PartialState()
        object HideLoader : PartialState()
        data class DisplayArticles(val articles: List<Article>) : PartialState()
    }
}
