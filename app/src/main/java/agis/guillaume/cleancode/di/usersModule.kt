
package agis.guillaume.cleancode.di

import agis.guillaume.cleancode.api.services.UserService
import agis.guillaume.cleancode.datastore.user.UserDatastore
import agis.guillaume.cleancode.datastore.user.UserLocalDatastore
import agis.guillaume.cleancode.repository.IUsersRepository
import agis.guillaume.cleancode.repository.UsersRepository
import agis.guillaume.cleancode.usecases.UsersUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

// deps to inject related to the users
val usersModule = module {

    factory<UserDatastore> { UserLocalDatastore(userDao = get()) }
    factory<IUsersRepository> { UsersRepository(
        service = get<Retrofit>(named(POSTS_RETROFIT_ARG)).create(
            UserService::class.java
        )
    )
    }
    factory { UsersUseCase(usersRepository = get(), userDatastore = get()) }
}