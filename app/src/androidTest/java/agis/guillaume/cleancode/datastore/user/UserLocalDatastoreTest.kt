package agis.guillaume.cleancode.datastore.user

import agis.guillaume.cleancode.datastore.MyAppDatabase
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
internal class UserLocalDatastoreTest : TestCase() {

    private lateinit var userDao: UserDao
    private lateinit var db: MyAppDatabase

    private val user = User(
        id = 32,
        website = "http://www.hire-me-maybe.com",
        username = "skategui",
        email = "firstname.lastname@gmail.com",
        phone = "21312-12301-2313"
    )


    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MyAppDatabase::class.java
        ).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadUser() = kotlinx.coroutines.test.runTest {
        val datastore = UserLocalDatastore(userDao)

        datastore.saveUser(user)
        val itemFound = datastore.getUserByID(user.id)
        assertEquals(user, itemFound)
    }

    @Test
    fun cannotFindUser() = kotlinx.coroutines.test.runTest {
        val datastore = UserLocalDatastore(userDao)

        datastore.saveUser(user)
        val itemFound = datastore.getUserByID(100)
        assertEquals(null, itemFound)
    }
}


