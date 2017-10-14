package dk.shape.flock2.bindings

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import edu.upc.mcia.publications.data.remote.ApiManager

/**
 *
 * Flock2
 * ImageViewBindings
 *
 * Created on 01/07/2017
 * Copyright (c) 2017 SHAPE A/S. All rights reserved.
 *
 */
object ImageViewBindings {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImageUrl(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
                .load(String.format(ApiManager.IMAGE_BASE_URL, url))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("circularImageUrl")
    fun setCircularImageUrl(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
                .load(String.format(ApiManager.IMAGE_BASE_URL, url))
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
    }
}