package agis.guillaume.cleancode.di

import agis.guillaume.cleancode.datastore.MyAppDatabase
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

// deps to inject related to the local DB
val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            MyAppDatabase::class.java,
            "myapp_database"
        ).build()
    }

    single {
        val database = get<MyAppDatabase>()
        database.postDao()
    }

    single {
        val database = get<MyAppDatabase>()
        database.userDao()
    }

    single {
        val database = get<MyAppDatabase>()
        database.articleDao()
    }
}