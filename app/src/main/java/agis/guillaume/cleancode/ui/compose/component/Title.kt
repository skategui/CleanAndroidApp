package agis.guillaume.cleancode.ui.compose.component

import agis.guillaume.cleancode.ui.compose.ColorPrimary
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Title(@StringRes id: Int) {
    Text(
        modifier = Modifier.padding(horizontal = 24.dp),
        textAlign = TextAlign.Center,
        text = stringResource(id = id),
        fontSize = 24.sp,
        color = ColorPrimary
    )
}

@Composable
fun Title(msg: String) {
    Text(
        modifier = Modifier.padding(horizontal = 24.dp),
        textAlign = TextAlign.Center,
        text = msg,
        fontSize = 24.sp,
        color = ColorPrimary
    )
}