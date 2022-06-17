/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.app.nowinandroid.core.playback.models

import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.support.v4.media.session.MediaControllerCompat
import com.google.samples.app.nowinandroid.core.playback.currentIndex
import com.google.samples.apps.nowinandroid.core.database.model.StationEntity
import com.google.samples.apps.nowinandroid.core.database.model.StationId
import com.google.samples.apps.nowinandroid.core.database.model.Stations
import com.google.samples.apps.nowinandroid.core.model.data.Station


data class PlaybackQueue(
    val ids: List<StationId> = emptyList(),
    val stations: List<Stations> = emptyList(),
    val title: String? = null,
    val initialMediaId: String = "",
    val currentIndex: Int = 0,
    val isIndexValid: Boolean = true
) : List<Stations> by stations {

    val isValid = ids.isNotEmpty() && stations.isNotEmpty() && currentIndex >= 0
    val isLastStation = currentIndex == lastIndex

    val currentStation get() = get(currentIndex)

    data class NowPlayingStation(val station: StationEntity, val index: Int) {
        companion object {

            fun from(queue: PlaybackQueue) = NowPlayingStation(queue.currentStation.get(0), queue.currentIndex)

            fun NowPlayingStation?.isCurrentStation(station: Station, audioIndex: Int? = null) =
                (this?.station?.stationuuid == station.stationuuid && (audioIndex == null || audioIndex == this.index))
        }
    }
}

fun fromMediaController(mediaController: MediaControllerCompat) = PlaybackQueue(
    title = mediaController.queueTitle?.toString(),
    ids = mediaController.queue.mapNotNull { it.description.mediaId },
    initialMediaId = mediaController.metadata?.getString(METADATA_KEY_MEDIA_ID) ?: "",
    currentIndex = mediaController.playbackState.currentIndex
)
