package agis.guillaume.cleancode.ui.compose.composable

import agis.guillaume.cleancode.R
import agis.guillaume.cleancode.model.Article
import agis.guillaume.cleancode.ui.compose.ColorPrimary
import agis.guillaume.cleancode.ui.compose.ColorWhite
import agis.guillaume.cleancode.ui.compose.Shapes
import agis.guillaume.cleancode.ui.compose.component.MediumSpacer
import agis.guillaume.cleancode.ui.compose.component.SmallSpacer
import agis.guillaume.cleancode.ui.compose.component.Title
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import java.text.SimpleDateFormat
import java.util.*


// LIST ARTICLES CONTENT


@Composable
fun displayListArticlesContent(
    articles: List<Article>,
    openArticle: (article: Article) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MediumSpacer()
        Text(
            text = stringResource(id = R.string.latest_tech_news_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = ColorPrimary
        )
        MediumSpacer()
        LazyColumn(content = {
            items(articles) { item -> ArticleCardItem(item, openArticle) }
        })
    }
}

@Composable
private fun ArticleCardItem(article: Article, openArticle: (article: Article) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { openArticle(article) },
        elevation = 10.dp,
        shape = Shapes.medium
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
                modifier = Modifier.padding(horizontal = 12.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1,
                text = article.title,
            )
            SmallSpacer()
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                text = article.description,
            )
            SmallSpacer()
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                fontSize = 10.sp,
                text = stringResource(R.string.article_source, article.author, article.source),
            )
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val formattedDate = formatter.format(article.publishedAt)
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
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
    ) {}
}


// EMPTY LIST ARTICLES CONTENT


@Composable
fun displayEmptyArticleContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sad_smiley))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorWhite),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Title(id = R.string.error_no_article_available)
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