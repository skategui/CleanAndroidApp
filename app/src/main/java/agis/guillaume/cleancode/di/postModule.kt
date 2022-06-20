
package agis.guillaume.cleancode.di

import agis.guillaume.cleancode.api.services.PostService
import agis.guillaume.cleancode.api.services.UserService
import agis.guillaume.cleancode.datastore.post.PostsDatastore
import agis.guillaume.cleancode.datastore.post.PostsLocalDatastore
import agis.guillaume.cleancode.repository.IPostsRepository
import agis.guillaume.cleancode.repository.PostsRepository
import agis.guillaume.cleancode.ui.post.PostReducer
import agis.guillaume.cleancode.ui.post.PostsListViewModel
import agis.guillaume.cleancode.usecases.PostsUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

// deps to inject related to the posts
val postModule = module {

    factory<IPostsRepository> {
        PostsRepository(service = get<Retrofit>(named(POSTS_RETROFIT_ARG))
            .create(PostService::class.java)
        ) }
    factory { PostsUseCase(usersUseCase = get(), postsRepository = get(), postsDatastore = get()) }
    factory<PostsDatastore> { PostsLocalDatastore(postDao = get()) }
    viewModel { PostsListViewModel(postsUseCase = get(), postReducer = PostReducer()) }
}