package agis.guillaume.cleancode.ui.post.views

import agis.guillaume.cleancode.BuildConfig
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 * Custom view to encapsulate Glide utilisation
 * Only used for the viewbinding example
 */
class GlideImageView : AppCompatImageView {

    private var requestOptions = RequestOptions().apply {
        transform(CenterCrop(), RoundedCorners(roundingRadius))
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    /**
     * load an user avatar given it's ID
     *  @param userId id of the user to load
     */
    fun loadRoundedAvatar(userId: Int) {
        loadImage(userId)
    }

    /**
     * load an image from an external resource using an ID
     *  @param id id of the resource to load
     */
    private fun loadImage(id: Int) {
        Glide.with(context)
            .load("${BuildConfig.AVATAR_BASE_URL}?img=$id")
            .apply(requestOptions)
            .into(this)
    }


    companion object {
        private const val roundingRadius = 90
    }


}
