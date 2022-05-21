package com.example.gclef

import androidx.annotation.Keep

@Keep
data class Song(var songTitle: String? = null,
                var songDetail : String? = null,
                var imageUrl : String = "",
                var uid : String? = null)