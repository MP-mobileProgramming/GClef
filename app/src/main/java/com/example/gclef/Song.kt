package com.example.gclef

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Song(var userName : String? = null,
                var songTitle: String? = null,
                var songDetail : String? = null,
                var imageUrl : String = "",
                var soundUrl : String = "",
                var uid : String? = null) : Serializable {

                }