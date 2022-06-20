package agis.guillaume.cleancode.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Post(
    val user: User?,
    val id: Int,
    val title: String,
    val body: String
) : Parcelable
