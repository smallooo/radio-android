/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.core.domain

import kotlinx.serialization.json.Json

val DEFAULT_JSON_FORMAT = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

val JSON = DEFAULT_JSON_FORMAT
