package agis.guillaume.cleancode.ui.compose.composable

import agis.guillaume.cleancode.R
import agis.guillaume.cleancode.ui.compose.ColorWhite
import agis.guillaume.cleancode.ui.compose.component.CTAButton
import agis.guillaume.cleancode.ui.compose.component.MediumSpacer
import agis.guillaume.cleancode.ui.compose.component.Title
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*


// ERROR CONTENT

@Composable
fun displayErrorContent(errorMsg: String? = null) {
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

        Title(errorMsg ?: stringResource(id = R.string.error_try_again_later))
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(dimensionResource(id = R.dimen.animation_size).value.dp)
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
        modifier = Modifier
            .fillMaxSize()
            .background(ColorWhite),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Title(id = R.string.error_no_internet_connexion)
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(dimensionResource(id = R.dimen.animation_size).value.dp)
        )
        MediumSpacer()
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

