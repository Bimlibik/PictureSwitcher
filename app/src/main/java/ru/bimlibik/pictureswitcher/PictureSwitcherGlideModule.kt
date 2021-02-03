package ru.bimlibik.pictureswitcher

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class PictureSwitcherGlideModule : AppGlideModule() {

    override fun isManifestParsingEnabled() = false
}