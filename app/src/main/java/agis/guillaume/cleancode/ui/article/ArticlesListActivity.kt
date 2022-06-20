package agis.guillaume.cleancode.ui.article

import agis.guillaume.cleancode.model.Article
import agis.guillaume.cleancode.ui.compose.MainScreen
import agis.guillaume.cleancode.ui.compose.displayErrorContent
import agis.guillaume.cleancode.ui.post.PostsListContract
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest

import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception

class ArticlesListActivity : AppCompatActivity() {

    private val viewModel: ArticlesListViewModel by viewModel()

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ArticlesListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainScreen(viewModel = viewModel) }
        initObservers()
    }

    private fun initObservers() {
        // Collect single event
        lifecycleScope.launchWhenStarted {
            viewModel.singleEvent.collectLatest { event ->
                when (event) {
                    is ArticlesListContract.SingleEvent.OpenArticle -> displayArticle(event.url)
                }
            }
        }
    }

    private fun displayArticle(url : String) {
        val newUrl = if (!url.startsWith("http://") && !url.startsWith("https://"))
            "http://$url"
        else url

        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(newUrl))
            ContextCompat.startActivity(this, browserIntent, null)
        } catch (e : Exception) {
            // no activity detected
        }

    }
}