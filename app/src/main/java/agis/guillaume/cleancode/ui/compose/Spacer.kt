package agis.guillaume.cleancode.ui.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SmallSpacer() {
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun MediumSpacer() {
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun BigSpacer() {
    Spacer(modifier = Modifier.height(48.dp))
}