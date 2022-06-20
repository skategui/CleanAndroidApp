package agis.guillaume.cleancode.ui.post.adapter

import agis.guillaume.cleancode.R
import agis.guillaume.cleancode.databinding.ItemPostBinding
import agis.guillaume.cleancode.model.Post
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostsAdapter( private val onItemClicked: (post: Post) -> Unit) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>(){
    private val posts = ArrayList<Post>()
    private val diffCallback = DiffCallback()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holderHistory: PostViewHolder, position: Int) {
        holderHistory.update(posts[position], onItemClicked)
    }

    override fun getItemCount() = posts.size

    class PostViewHolder(private val holder: ItemPostBinding) : RecyclerView.ViewHolder(holder.root) {

        fun update(post: Post, onItemClicked: (post: Post) -> Unit) = with(holder) {
            post.user?.run {
                authorAvatar.loadRoundedAvatar(id)
                commentAuthorName.text = String.format(root.resources.getString(R.string.post_author_info), post.user.username)
            }
            commentTitle.text = post.title
            commentBody.text = post.body
            container.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch { onItemClicked(post) }
            }
        }
    }

    /**
     * Verify if there are some changes between the current and the new lists and then update the recyclerview with the difference
     */
     fun update(list: List<Post>) {
        diffCallback.compareLists(this.posts, list)
        val differenceFound = DiffUtil.calculateDiff(diffCallback)
        this.posts.clear()
        this.posts.addAll(list)
        differenceFound.dispatchUpdatesTo(this)
    }
}