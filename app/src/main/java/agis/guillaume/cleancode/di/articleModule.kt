package agis.guillaume.cleancode.di

import agis.guillaume.cleancode.api.services.ArticlesService
import agis.guillaume.cleancode.api.services.PostService
import agis.guillaume.cleancode.datastore.article.ArticlesDatastore
import agis.guillaume.cleancode.datastore.article.ArticlesLocalDatastore
import agis.guillaume.cleancode.repository.ArticlesRepository
import agis.guillaume.cleancode.repository.IArticlesRepository
import agis.guillaume.cleancode.ui.article.ArticleReducer
import agis.guillaume.cleancode.ui.article.ArticlesListViewModel
import agis.guillaume.cleancode.usecases.ArticlesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

// deps to inject related to the articles
val articleModule = module {

    factory<IArticlesRepository> {
        ArticlesRepository(
            service = get<Retrofit>(named(ARTICLE_RETROFIT_ARG))
                .create(ArticlesService::class.java)
        )
    }
    factory { ArticlesUseCase(articlesRepository = get(), articleDatastore = get()) }
    factory<ArticlesDatastore> { ArticlesLocalDatastore(articleDao = get()) }
    viewModel { ArticlesListViewModel(articlesUseCase = get(), articleReducer = ArticleReducer()) }
}