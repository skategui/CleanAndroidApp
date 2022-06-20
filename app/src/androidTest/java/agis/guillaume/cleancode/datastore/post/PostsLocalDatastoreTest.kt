package agis.guillaume.cleancode.datastore.post

import agis.guillaume.cleancode.datastore.MyAppDatabase
import agis.guillaume.cleancode.model.Post
import agis.guillaume.cleancode.model.User
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
internal class PostsLocalDatastoreTest : TestCase() {

    private lateinit var ostDao: PostDao
    private lateinit var db: MyAppDatabase

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MyAppDatabase::class.java
        ).build()
        ostDao = db.postDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadPosts() = kotlinx.coroutines.test.runTest {
        val datastore = PostsLocalDatastore(ostDao)

        val user = User(
            id = 32,
            website = "http://www.hire-me-maybe.com",
            username = "skategui",
            email = "firstname.lastname@gmail.com",
            phone = "21312-12301-2313"
        )

        val articles = listOf(
            Post(
                user = user,
                id = 42,
                title = "my_post",
                body = "that's the body"
            ),
            Post(
                user = user,
                id = 42,
                title = "my_post2",
                body = "that's another body"
            )
        )

        datastore.savePosts(articles)
        val itemsFound = datastore.getPosts()
        assertEquals(articles, itemsFound)
    }
}


