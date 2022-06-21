package agis.guillaume.cleancode.ui.select

import agis.guillaume.cleancode.ui.compose.composable.SelectOptionScreen
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity


class SelectOptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SelectOptionScreen() }
    }
}


