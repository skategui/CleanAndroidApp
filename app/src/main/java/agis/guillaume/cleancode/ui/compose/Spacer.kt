package agis.guillaume.cleancode.ui.compose

import agis.guillaume.cleancode.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp

/**
 * Button part of the design component . Those spacers can be reused
 * All modifications of those spacers here will update all the spacers in the app
 */
@Composable
fun SmallSpacer() {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_spacer).value.dp))
}

@Composable
fun MediumSpacer() {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_spacer).value.dp))
}

@Composable
fun BigSpacer() {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.big_spacer).value.dp))
}