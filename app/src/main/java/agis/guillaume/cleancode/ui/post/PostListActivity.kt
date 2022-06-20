package agis.guillaume.cleancode.ui.post

import agis.guillaume.cleancode.R
import agis.guillaume.cleancode.databinding.ActivityListPostsBinding
import agis.guillaume.cleancode.model.Post
import agis.guillaume.cleancode.ui.ext.gone
import agis.guillaume.cleancode.ui.ext.invisible
import agis.guillaume.cleancode.ui.ext.visible
import agis.guillaume.cleancode.ui.post.adapter.PostsAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable.INFINITE
import com.airbnb.lottie.LottieDrawable.REVERSE
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostListActivity : AppCompatActivity() {

    private val viewModel: PostsListViewModel by viewModel()
    private lateinit var binding: ActivityListPostsBinding
    private val adapter = PostsAdapter { viewModel.setEvent(PostsListContract.Interaction.PostClicked(it)) }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PostListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initObservers()
    }

    private fun initView(){
        // display message with no loaded list
        initAnimation()
        initRecyclerView()
        initViews()
    }

    /**
     * Init animation of the comment button to push the user to click on it
     */
    private fun initAnimation() = with(binding) {
        // set animation
        animation.repeatMode = REVERSE
        animation.repeatCount = INFINITE
    }

    /**
     * Init recycler view
     */
    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.postsList.layoutManager = layoutManager
        binding.postsList.itemAnimator = DefaultItemAnimator()
        binding.postsList.adapter = adapter
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {

            binding.reloadBtn.setOnClickListener { viewModel.setEvent(PostsListContract.Interaction.ReloadButtonClicked) }

            viewModel.uiState.collect { state -> currentState(state.posts, state.isLoading) }
        }

        // Collect single event
        lifecycleScope.launchWhenStarted {
            viewModel.singleEvent.collectLatest { event ->
                when (event) {
                    is PostsListContract.SingleEvent.DisplayErrorPopup -> displayError()
                    PostsListContract.SingleEvent.DisplayInternetLostMessage -> noInternet()
                    is PostsListContract.SingleEvent.DisplayPostDetail -> postDetailComingSoon(event.postSelected)
                }
            }
        }
    }

    private fun displayPosts(posts: List<Post>) {
        if (posts.isEmpty())
            displayEmptyListMessage()
        else
           adapter.update(posts)
    }

    /**
     * Inform the user to that the list of posts is empty
     */
    private fun displayEmptyListMessage() = with(binding) {
        stateContainer.visible()
        animation.setAnimation(R.raw.empty_list_animation)
        stateTitle.text = getString(R.string.error_no_post_available)
        animation.playAnimation()
        hideRecyclerView()
    }


    /**
     * Inform the user to the error
     */
    private fun displayError() = with(binding) {
        stateContainer.visible()
        animation.setAnimation(R.raw.error_animation)
        stateTitle.text = getString(R.string.error_try_again_later)
        animation.playAnimation()
        hideRecyclerView()
    }

    /**
     * put the views in their original state
     */
    private fun initViews()  = with(binding) {
        currentState(emptyList(), false)
        animation.clearAnimation()
        postsList.visible()
        reloadBtn.gone()
    }

    /**
     * Check the state of the loading animation
     * @param isLoading true if the request is still loading, false otherwise
     */
    private fun currentState(posts : List<Post>, isLoading: Boolean) = with(binding){
        if (isLoading) {
            animation.setAnimation(R.raw.loading_animation)
            animation.playAnimation()
            stateTitle.text = getString(R.string.loading_in_progress)
           // stateContainer.visibility = View.VISIBLE
            hideRecyclerView()
        } else {
            stateContainer.gone()
            postsList.visible()
            displayPosts(posts)
        }
    }

    /**
     * Inform the user to that he lost the internet connexion
     */
    private fun noInternet()  = with(binding){
        stateContainer.visible()
        stateTitle.text = getString(R.string.error_no_internet_connexion)
        animation.setAnimation(R.raw.error_animation)
        reloadBtn.visible()
        animation.playAnimation()
        hideRecyclerView()
    }

    /**
     * hide recycler view
     */
    private fun hideRecyclerView() {
        binding.postsList.invisible()
    }

    /**
     * Display coming soon message
     * @param post post  to display the detail from
     */
    private fun postDetailComingSoon(post: Post) {
       Toast.makeText(this, String.format(getString(R.string.coming_soon), post.title), Toast.LENGTH_LONG).show()
    }
}
