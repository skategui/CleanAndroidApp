package agis.guillaume.cleancode.ui.compose.composable

import agis.guillaume.cleancode.R
import agis.guillaume.cleancode.ui.compose.ColorWhite
import agis.guillaume.cleancode.ui.compose.component.BigSpacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*


// LOADING STATE CONTENT

@Composable
fun LoadingStateContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
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

        Title(id = R.string.loading_in_progress)
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
