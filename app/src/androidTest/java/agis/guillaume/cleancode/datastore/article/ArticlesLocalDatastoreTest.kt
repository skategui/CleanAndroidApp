package agis.guillaume.cleancode.datastore.article

import agis.guillaume.cleancode.datastore.MyAppDatabase
import agis.guillaume.cleancode.model.Article
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import kotlinx.coroutines.flow.onEach
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class ArticlesLocalDatastoreTest : TestCase() {

    private lateinit var articleDao: ArticleDao
    private lateinit var db: MyAppDatabase

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MyAppDatabase::class.java
        ).build()
        articleDao = db.articleDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadArticles() = kotlinx.coroutines.test.runTest {
        val datastore = ArticlesLocalDatastore(articleDao)
        val articles = listOf(
            Article(
                source = "TechCrunch",
                author = "Guillaume Agis",
                title = "Another brutal week for crypto and crypto companies",
                description = "Hi all! Welcome back to Week in Review, the newsletter where we recap the most read stories to cross TechCrunch over the last week. Our goal: If you’ve had a busy few days, you should be able to click into this on Saturday, give it a skim, and still have a pretty good idea of what went down this week.",
                url = "https://techcrunch.com/2022/06/18/another-brutal-week-for-crypto-and-crypto-companies/",
                urlToImage = "https://techcrunch.com/wp-content/uploads/2022/05/GettyImages-1371430596.jpg?w=1390&crop=1",
                publishedAt = Date()
            ),
            Article(
                source = "Forbe",
                author = "Elon Musk",
                title = "Elon announces Tesla will accept DogeCoin ",
                description = "Desc",
                url = "url",
                urlToImage = "image_url",
                publishedAt = Date()
            )
        )
         datastore.getArticles()
            .onEach { assertEquals(articles, it) }
        datastore.saveArticles(articles)


    }
}

