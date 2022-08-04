package com.dmhsh.samples.apps.nowinandroid.core.data.repository.local

import com.dmhsh.samples.apps.nowinandroid.core.network.model.NetworkTopic
import org.intellij.lang.annotations.Language

object LocalStationSource {
    val sampleTopic = NetworkTopic(
        id = "2",
        name = "Headlines",
        shortDescription = "News we want everyone to see",
        longDescription = "Stay up to date with the latest events and announcements from Android!",
        url = "",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/now-in-android.appspot.com/o/img%2Fic_topic_Headlines.svg?alt=media&token=506faab0-617a-4668-9e63-4a2fb996603f"
    )


    @Language("JSON")
    val topVoted = """
      [
          {
              "changeuuid": "7a97f96e-8247-4020-9a6b-1fbe0517be19",
              "stationuuid": "78012206-1aa1-11e9-a80b-52543be04c81",
              "serveruuid": "a67a5b07-6189-4442-b5e8-0100f7f56804",
              "name": "MANGORADIO",
              "url": "http://stream.mangoradio.de/",
              "url_resolved": "https://mangoradio.stream.laut.fm/mangoradio?t302=2022-08-04_01-59-55&uuid=4e26ed11-a98f-45a3-9394-d0c80bed72e0",
              "homepage": "https://mangoradio.de/",
              "favicon": "https://mangoradio.de//android-icon-192x192.png",
              "tags": "mango,mangoradio,mongo,mongoradio,public radio,webradio",
              "country": "Germany",
              "countrycode": "DE",
              "iso_3166_2": "",
              "state": "",
              "language": "german",
              "languagecodes": "de",
              "votes": "536214",
              "lastchangetime": "",
              "lastchangetime_iso8601": "2022-03-28T08:52:35Z",
              "codec": "MP3",
              "bitrate": "128",
              "hls": "0",
              "lastcheckok": "1",
              "lastchecktime": "2022-08-04 00:00:14",
              "lastchecktime_iso8601": "2022-08-04T00:00:14Z",
              "lastcheckoktime": "2022-08-04 00:00:14",
              "lastcheckoktime_iso8601": "2022-08-04T00:00:14Z",
              "lastlocalchecktime": "2022-08-04 00:00:14",
              "lastlocalchecktime_iso8601": "2022-08-04T00:00:14Z",
              "clicktimestamp": "2022-08-04 02:36:36",
              "clicktimestamp_iso8601": "2022-08-04T02:36:36Z",
              "clickcount": "3992",
              "clicktrend": "-19",
              "ssl_error": "0",
              "geo_lat": "",
              "geo_long": "",
              "has_extended_info": "false"
          }
      ]
    """.trimIndent()

}