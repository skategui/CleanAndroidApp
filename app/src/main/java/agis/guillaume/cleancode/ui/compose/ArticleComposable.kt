package agis.guillaume.cleancode.ui.compose

import agis.guillaume.cleancode.R
import agis.guillaume.cleancode.model.Article
import agis.guillaume.cleancode.ui.article.ArticlesListActivity
import agis.guillaume.cleancode.ui.article.ArticlesListContract
import agis.guillaume.cleancode.ui.article.ArticlesListViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun MainScreen(
    viewModel: ArticlesListViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val singleEvent by viewModel.singleEvent.collectAsState(null)

    when {
        state.isLoading -> LoadingStateContent()
        state.articles.isNotEmpty() -> displayListArticlesContent(state.articles) {article -> viewModel.setEvent(
            ArticlesListContract.Event.ArticleClicked(article)
        )}
        state.articles.isEmpty() -> displayEmptyArticleContent()
    }

    when (singleEvent) {
        is ArticlesListContract.SingleEvent.DisplayErrorPopup -> displayErrorContent()
        ArticlesListContract.SingleEvent.DisplayInternetLostMessage -> displayNoInternetContent()
        else -> {}
    }
}

// LOADING STATE CONTENT

@Composable
fun LoadingStateContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.loading_in_progress),
            fontSize = 24.sp,
            color = colorResource(id = R.color.colorPrimary)
        )
        BigSpacer()
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(dimensionResource(id = R.dimen.animation_size).value.dp)
        )
    }
}

@Composable
@Preview
fun loadingPreview() {
    LoadingStateContent()
}


// LIST ARTICLES CONTENT



@Composable
fun displayListArticlesContent(articles: List<Article>, openArticle : (article: Article) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MediumSpacer()
        Text(
            text = stringResource(id = R.string.latest_tech_news_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.colorPrimary)
        )
        MediumSpacer()
        LazyColumn(content = {
            items(articles) { item -> ArticleCardItem(item, openArticle) }
        })
    }
}

@Composable
private fun ArticleCardItem(article: Article, openArticle : (article: Article) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { openArticle(article) },
        elevation = 10.dp
    ) {
        Column {
            AsyncImage(
                model = article.urlToImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(R.string.article_image_content_description)
            )
            SmallSpacer()
            Text(
                modifier = Modifier.padding(horizontal = 15.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1,
                text = article.title,
            )
            SmallSpacer()
            Text(
                modifier = Modifier.padding(horizontal = 15.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                text = article.description,
            )
            SmallSpacer()
            Text(
                modifier = Modifier.padding(horizontal= 15.dp),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                fontSize = 10.sp,
                text = stringResource(R.string.article_source, article.author, article.source),
            )
            val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm")
            val formattedDate = formatter.format(article.publishedAt)
            Text(
                modifier = Modifier.padding(horizontal = 15.dp),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                fontSize = 10.sp,
                text = formattedDate,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
@Preview
fun listPreview() {
    displayListArticlesContent(
        articles = listOf(
            Article(
                source = "TechCrunch",
                author = "Guillaume Agis",
                title = "Another brutal week for crypto and crypto companies",
                description = "Hi all! Welcome back to Week in Review, the newsletter where we recap the most read stories to cross TechCrunch over the last week. Our goal: If you’ve had a busy few days, you should be able to click into this on Saturday, give it a skim, and still have a pretty good idea of what went down this week.",
                url = "https://techcrunch.com/2022/06/18/another-brutal-week-for-crypto-and-crypto-companies/",
                urlToImage = "https://techcrunch.com/wp-content/uploads/2022/05/GettyImages-1371430596.jpg?w=1390&crop=1",
                publishedAt = Date()

            )
        )
    ){}
}



// EMPTY LIST ARTICLES CONTENT


@Composable
fun displayEmptyArticleContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_list_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.error_no_article_available),
            fontSize = 24.sp,
            color = colorResource(id = R.color.colorPrimary)
        )
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(dimensionResource(id = R.dimen.animation_size).value.dp)
        )
    }
}

@Composable
@Preview
fun emptyListPreview() {
    displayEmptyArticleContent()
}



// ERROR CONTENT

@Composable
fun displayErrorContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.error_try_again_later),
            fontSize = 24.sp,
            color = colorResource(id = R.color.colorPrimary)
        )
        BigSpacer()
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
@Preview
fun displayErrorPreview() {
    displayErrorContent()
}


// NO INTERNET CONTENT

@Composable
fun displayNoInternetContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.error_no_internet_connexion),
            fontSize = 24.sp,
            color = colorResource(id = R.color.colorPrimary)
        )
        BigSpacer()
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(100.dp)
        )
        BigSpacer()
        CTAButton(
            labelResID = R.string.refresh,
            onClick = {
                // TO DO
            })
    }
}


@Composable
@Preview
fun displayNoInternetPreview() {
    displayNoInternetContent()
}

