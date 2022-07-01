/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.core.util


import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

val API_ZONE_ID: ZoneId = ZoneId.of("Asia/Ashgabat")
val HOUR_MINUTES_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun apiNow() = ZonedDateTime.now(API_ZONE_ID)

fun String?.apiDate(fallback: ZonedDateTime = ZonedDateTime.now()): ZonedDateTime {
    return try {
        OffsetDateTime.parse(this).atZoneSameInstant(API_ZONE_ID)
    } catch (e: Exception) {
        //RemoteLogger.exception(e)
        return fallback
    }
}

fun String?.toFormattedDateFromApi(dateFormat: DateTimeFormatter = DateTimeFormatter.BASIC_ISO_DATE): String = dateFormat.format(apiDate())
fun String?.toTimeFromApi(): String = HOUR_MINUTES_FORMAT.format(apiDate())

fun serverTime() = ZonedDateTime.now(API_ZONE_ID)
