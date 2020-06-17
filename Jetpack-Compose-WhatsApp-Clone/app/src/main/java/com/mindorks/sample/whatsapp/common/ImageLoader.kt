package com.mindorks.sample.whatsapp.common

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.*
import androidx.ui.core.ContentScale
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredHeightIn
import androidx.ui.layout.preferredSize
import androidx.ui.res.loadVectorResource
import androidx.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

object ImageLoader {

    @Composable
    fun load(@DrawableRes imageUrl: Int, modifier: Modifier, onClick: (Boolean) -> Unit) {

        val vectorAsset = loadVectorResource(imageUrl)
        vectorAsset.resource.resource?.let {
            Clickable(onClick = {
                onClick(true)
            }) {
                Image(
                    asset = it,
                    modifier = modifier,
                    contentScale = ContentScale.Fit

                )
            }
        }
    }

    @Composable
    fun load(url: String) {

        var image by state<ImageAsset?> { null }
        var drawable by state<Drawable?> { null }
        val context = ContextAmbient.current
        onCommit(url) {
            val glide = Glide.with(context)
            val target = object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    image = null
                    drawable = placeholder
                }

                override fun onResourceReady(
                    bitmap: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    image = bitmap.asImageAsset()
                }
            }
            glide
                .asBitmap()
                .load(url)
                .into(target)
            onDispose {
                image = null
                drawable = null
                glide.clear(target)
            }
        }
        val theImage = image
        val theDrawable = drawable
        if (theImage != null) {
            Box(
                modifier = Modifier.preferredHeightIn(maxHeight = 40.dp) + Modifier.preferredHeightIn(
                    maxHeight = 40.dp
                ),
                gravity = ContentGravity.Center
            ) {
                Image(
                    asset = theImage,
                    modifier = Modifier.preferredSize(40.dp)
                        .clip(shape = RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        } else if (theDrawable != null) {
            Canvas(modifier = Modifier.preferredHeight(40.dp) + Modifier.fillMaxWidth()) {
                theDrawable.draw(this.nativeCanvas)
            }
        }
    }
}