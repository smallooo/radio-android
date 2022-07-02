/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.core.util

import android.content.Context

fun <T> Context.systemService(name: String): T {
    return getSystemService(name) as T
}
