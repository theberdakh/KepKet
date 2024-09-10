package com.theberdakh

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.theberdakh.kepket.R

class GlideLoader(private val context: Context) {

    fun ImageView.loadImage(url: String, @DrawableRes placeholder: Int = R.drawable.logo){
        Glide.with(context)
            .load(url)
            .placeholder(placeholder)
            .into(this)
    }
}