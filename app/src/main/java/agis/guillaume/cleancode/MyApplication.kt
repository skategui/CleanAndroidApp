package agis.guillaume.cleancode

import agis.guillaume.cleancode.di.*
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // init koin, deps inject tool
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    networkModule,
                    postModule,
                    usersModule,
                    databaseModule,
                    articleModule
                )
            )
        }
    }
}