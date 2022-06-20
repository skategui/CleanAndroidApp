package agis.guillaume.cleancode.ui.select


import agis.guillaume.cleancode.R
import agis.guillaume.cleancode.ui.article.ArticlesListActivity
import agis.guillaume.cleancode.ui.compose.CTAButton
import agis.guillaume.cleancode.ui.post.PostListActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(96.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.logo_content_description)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            stringResource(R.string.intro_welcome_msg),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.colorPrimary),
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(48.dp))
        CTAButton(
            labelResID = R.string.intro_new_collection,
            onClick = {
                ArticlesListActivity.start(context)
            })
        Spacer(modifier = Modifier.height(24.dp))
        CTAButton(
            labelResID = R.string.intro_old_collection,
            onClick = {
                PostListActivity.start(context)
            })
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            text = stringResource(R.string.intro_author),
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
@Preview
fun displayMainScreenPreview(){
    MainScreen()
}