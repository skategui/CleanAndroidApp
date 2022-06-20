package agis.guillaume.cleancode.di

import agis.guillaume.cleancode.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// variables used in order to inject the right retrofit
val POSTS_RETROFIT_ARG = "POSTS_RETROFIT_ARG"
val ARTICLE_RETROFIT_ARG = "ARTICLES_RETROFIT_ARG"

// deps to inject related to the network requests
val networkModule = module {

    fun providePostsRetrofit(baseUrl: String): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG)
                    level = HttpLoggingInterceptor.Level.BODY
            })
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()


        val builder = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
        return builder.build()
    }

    fun provideArticlesRetrofit(baseUrl: String): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG)
                    level = HttpLoggingInterceptor.Level.BODY
            })
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()


        val builder = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
        return builder.build()
    }

    single(named(POSTS_RETROFIT_ARG)) { providePostsRetrofit(baseUrl =  BuildConfig.API_BASE_URL)}
    single(named(ARTICLE_RETROFIT_ARG)) { provideArticlesRetrofit(baseUrl =  BuildConfig.NEWS_BASE_URL)}
}