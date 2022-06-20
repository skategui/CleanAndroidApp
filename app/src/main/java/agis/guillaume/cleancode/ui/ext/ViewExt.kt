package agis.guillaume.cleancode.ui.ext

import android.view.View

// extension function in order to update the visibility of the views and make the code more readable

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}