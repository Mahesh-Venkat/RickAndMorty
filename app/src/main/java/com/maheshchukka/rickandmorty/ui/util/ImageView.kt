package com.maheshchukka.rickandmorty.ui.util

import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation
import com.maheshchukka.rickandmorty.R

fun ImageView.loadImage(imageUrl: String?) {
    this.load(imageUrl) {
        crossfade(true)
        placeholder(R.drawable.ic_placeholder)
        error(R.drawable.ic_broken_image)
        transformations(CircleCropTransformation())
    }
}