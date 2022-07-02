package com.dmhsh.samples.apps.nowinandroid.core.model.data

import android.nfc.Tag

data class FollowableTag(
    val tag: StationsTag,
    val isFollowed: Boolean
)