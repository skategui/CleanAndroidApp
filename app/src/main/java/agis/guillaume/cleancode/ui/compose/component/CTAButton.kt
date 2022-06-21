package agis.guillaume.cleancode.ui.compose.component

import agis.guillaume.cleancode.R
import agis.guillaume.cleancode.ui.compose.Shapes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Button part of the design component . This button can be reused
 * All modifications of the button here will update all the buttons in the app
 */
@Composable
fun CTAButton(@StringRes labelResID: Int, onClick: () -> Unit) {
    Button(
        modifier = Modifier.size(
            dimensionResource(id = R.dimen.btn_width).value.dp,
            height = dimensionResource(id = R.dimen.btn_height).value.dp
        ),
        onClick = { onClick.invoke() },
        border = BorderStroke(1.dp, colorResource(R.color.colorPrimary)),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = colorResource(R.color.colorPrimary),
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
            disabledElevation = 0.dp
        ),
        shape = Shapes.large
    ) {
        Text(
            text = stringResource(labelResID),
            fontSize = dimensionResource(id = R.dimen.btn_textsize).value.sp,
        )
    }
}