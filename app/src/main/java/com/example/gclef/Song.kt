package com.example.gclef

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Song(var userName : String? = null,
                var songTitle: String? = null,
                var songDetail : String? = null,
                var imageUrl : String? = null,
                var soundUrl : String? = null,
                var path : String? = null,
                var highLightTime : Int = 0,
                var timeStamp: Long = 1,
                var uid : String? = null) : Serializable {

                }