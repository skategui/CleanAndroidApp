package agis.guillaume.cleancode.ui.compose.composable

import agis.guillaume.cleancode.R
import agis.guillaume.cleancode.ui.compose.ColorWhite
import agis.guillaume.cleancode.ui.compose.Shapes
import agis.guillaume.cleancode.ui.compose.component.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.*


// ERROR CONTENT

@Composable
fun displayErrorDialog(
    errorMsg: MutableState<String?>
) {
    if (errorMsg.value != null) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sad_smiley))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )

        Dialog(
            onDismissRequest = { errorMsg.value = null },
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Surface(
                shape = Shapes.medium,
                color = ColorWhite
            ) {
                Column(
                    modifier = Modifier.background(ColorWhite),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    BigSpacer()
                    Title(errorMsg.value ?: stringResource(id = R.string.error_try_again_later))
                    LottieAnimation(
                        composition,
                        progress,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.animation_size).value.dp)
                    )
                    SmallSpacer()
                    CTAButton(
                        labelResID = R.string.cancel,
                        onClick = { errorMsg.value = null })
                    BigSpacer()
                }
            }
        }
    }
}

// NO INTERNET CONTENT

@Composable
fun displayNoInternetDialog(
    visible: MutableState<Boolean>,
    onButtonClicked: () -> Unit
) {
    if (visible.value) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error_animation))
        val progress by animateLottieCompositionAsState(composition)

        Dialog(
            onDismissRequest = { visible.value = false },
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Surface(
                shape = Shapes.medium,
                color = ColorWhite
            ) {
                Column(
                    modifier = Modifier
                        .background(ColorWhite),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    MediumSpacer()
                    Title(id = R.string.error_no_internet_connexion)
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
                            onButtonClicked()
                            visible.value = false
                        })
                    SmallSpacer()
                    CTAButton(
                        labelResID = R.string.cancel,
                        onClick = { visible.value = false })
                    BigSpacer()
                }
            }
        }
    }
}