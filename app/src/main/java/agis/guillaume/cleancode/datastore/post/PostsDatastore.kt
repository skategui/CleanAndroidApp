package agis.guillaume.cleancode.datastore.post

import agis.guillaume.cleancode.model.Post

/**
 * PostsLocalDatastore is responsible to store the posts into the local DB (using android Room)
 */
internal class PostsLocalDatastore(private val postDao: PostDao) : PostsDatastore {

    /**
     * Clean the DB and stored the latest version of the posts
     *  @param posts list of posts to store
     */
   override suspend fun savePosts(posts: List<Post>) {
        postDao.deleteAllPosts()
        postDao.insertAllPosts(posts.map { post -> post.toPostEntity() })
    }

    /**
     * Get all the stored posts
     *  @return [List] [Post]  list of posts fetched from server
     */
    override suspend fun getPosts() = postDao.getPosts().map { entity -> entity.toPost() }
}


/**
 * PostsDatastore is is an interface to hide the implementation of the PostsLocalDatastore.
 * PS : it's a bit over engineered for this app, but useful for the unit tests :)
 */
interface PostsDatastore{
    suspend fun savePosts(posts: List<Post>)
    suspend fun getPosts() : List<Post>
}