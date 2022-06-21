package agis.guillaume.cleancode.ui.article

import agis.guillaume.cleancode.api.utils.HttpErrorUtils
import agis.guillaume.cleancode.api.utils.doIfFailure
import agis.guillaume.cleancode.base.BaseViewModel
import agis.guillaume.cleancode.tracker.Tracker
import agis.guillaume.cleancode.usecases.ArticlesUseCase
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ArticlesListViewModel is responsible to handle the interaction between the user case(handling the business logic)
 * and what's happening on the UI side (display latest state, handle user interactions etc....)
 * It will display automatically the data stored in the local DB. The fetch from the remote server will only
 * store the data in the DB, so the flow will emit the latest version.
 */
class ArticlesListViewModel(
    private val articlesUseCase: ArticlesUseCase,
    private val articleReducer: ArticleReducer,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO // its a good practice to have the dispatcher as a param, as it's also useful for the unit test
) :
    BaseViewModel<ArticlesListContract.Interaction, ArticlesListContract.State, ArticlesListContract.SingleEvent>(
        ArticlesListContract.State()
    ) {

    override fun onCreate(owner: LifecycleOwner) {
        fetchArticles()
        renderLoadedArticles()
    }

    /**
     * Fetch the latest articles from the server (will store in the local DB )
     */
    private fun fetchArticles() {
        viewModelScope.launch(ioDispatcher) {
            articlesUseCase.loadArticles()
                .onEach {
                    setState {
                        articleReducer.reduce(
                            this,
                            ArticleReducer.PartialState.DisplayLoader
                        )
                    }
                }
                .collect { res ->
                    setState { articleReducer.reduce(this, ArticleReducer.PartialState.HideLoader) }
                    res.doIfFailure { error, throwable ->
                        handleErrors(error, throwable)
                    }
                }
        }
    }

    /**
     * Render the articles loaded in the local DB (Local DB will emit automatically as the table will be updated )
     */
    private fun renderLoadedArticles() {
        viewModelScope.launch(ioDispatcher) {
            articlesUseCase.getLoadedArticles()
                .collect { res ->
                    setState {
                        articleReducer.reduce(
                            this,
                            ArticleReducer.PartialState.DisplayArticles(res)
                        )
                    }
                }
        }
    }

    /**
     * Handle errors thrown while fetching the articles , emit the event associated and track the error
     *  @param error error message, is exist. Null if not existing
     *  @param throwable error thrown. Null if not existing
     */
    private fun handleErrors(error: String?, throwable: Throwable?) {
        setState {
            if (HttpErrorUtils.hasLostInternet(throwable))
                articleReducer.reduce(this, ArticleReducer.PartialState.DisplayInternetLostMsg)
            else
                articleReducer.reduce(this, ArticleReducer.PartialState.DisplayErrorMsg(error))
        }
        Tracker.trackError(throwable)
    }

    /**
     * EVent received from the user interaction, from the UI
     */
    override fun subscribeUserInteraction(event: ArticlesListContract.Interaction) {
        when (event) {
            is ArticlesListContract.Interaction.ArticleClicked -> setSingleEvent {
                ArticlesListContract.SingleEvent.OpenArticle(event.article.url)
            }
            is ArticlesListContract.Interaction.ReloadButtonClicked -> fetchArticles()
        }
    }
}