package com.example.gclef

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Song(var userName : String? = null,
                var songTitle: String? = null,
                var songDetail : String? = null,
                var imageUrl : String? = null,
                var soundUrl : String? = null,
                var uid : String? = null) : Serializable {

                }