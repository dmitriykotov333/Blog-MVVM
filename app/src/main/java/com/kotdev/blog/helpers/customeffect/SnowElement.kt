package com.kotdev.blog.helpers.customeffect

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi


class SnowElement {
    val drawable: Drawable
    val color: Int

    constructor(drawable: Drawable, color: Int) {
        this.drawable = drawable
        this.color = color
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    constructor(context: Context?, @DrawableRes drawableRes: Int, @ColorRes colorRes: Int) {
        drawable = context?.getDrawable(drawableRes)!!
        color = context.getColor(colorRes)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    constructor(context: Context?, @DrawableRes drawableRes: Int) {
        drawable = context?.getDrawable(drawableRes)!!
        color = 0
    }
}