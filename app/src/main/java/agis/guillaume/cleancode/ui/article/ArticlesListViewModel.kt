package agis.guillaume.cleancode.ui.article

import agis.guillaume.cleancode.api.utils.HttpErrorUtils
import agis.guillaume.cleancode.api.utils.doIfFailure
import agis.guillaume.cleancode.api.utils.doIfSuccess
import agis.guillaume.cleancode.base.BaseViewModel
import agis.guillaume.cleancode.tracker.Tracker
import agis.guillaume.cleancode.usecases.ArticlesUseCase
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * ArticlesListViewModel is responsible to handle the interaction between the user case(handling the business logic)
 * and what's happening on the UI side (display latest state, handle user interactions etc....)
 */
class ArticlesListViewModel(
    private val articlesUseCase: ArticlesUseCase,
    private val articleReducer: ArticleReducer,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO // its a good practice to have the dispatcher as a param, as it's also useful for the unit test
) :
    BaseViewModel<ArticlesListContract.Interaction, ArticlesListContract.State, ArticlesListContract.SingleEvent>() {

    init {
        fetchArticles()
        renderLoadedArticles()
    }

    /**
     * Fetch the latest articles from the server (will store in the local DB )
     */
    private fun fetchArticles() {
        viewModelScope.launch(ioDispatcher) {
            articlesUseCase.loadArticles()
                .onStart {
                    setState { articleReducer.reduce(this, ArticleReducer.PartialState.DisplayLoader)
                    }
                }
                .collect { res ->
                    res.doIfFailure { error, throwable ->
                        setState { articleReducer.reduce(this, ArticleReducer.PartialState.HideLoader ) }
                        handleErrors(error, throwable)
                    }
                    res.doIfSuccess {
                        setState { articleReducer.reduce(this, ArticleReducer.PartialState.HideLoader) }
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
        if (HttpErrorUtils.hasLostInternet(throwable))
            setSingleEvent { ArticlesListContract.SingleEvent.DisplayInternetLostMessage }
        else
            setSingleEvent { ArticlesListContract.SingleEvent.DisplayErrorPopup(error) }
        Tracker.trackError(throwable)
    }

    override fun createInitialState() = ArticlesListContract.State()

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