/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.core.util

import kotlinx.coroutines.CoroutineDispatcher

data class CoroutineDispatchers(
    val network: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val main: CoroutineDispatcher,
)
