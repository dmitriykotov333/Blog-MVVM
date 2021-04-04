package com.kotdev.blog.helpers.customeffect

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView


class SnowEffectImage : AppCompatImageView {
    constructor(@NonNull context: Context?) : super(context!!) {}
    constructor(@NonNull context: Context?, @Nullable attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
    }

    constructor(
        @NonNull context: Context?,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
    }
}