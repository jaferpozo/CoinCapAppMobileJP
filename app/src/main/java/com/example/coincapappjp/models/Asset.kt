package com.example.coincapappjp.models

import android.health.connect.datatypes.units.Percentage

data class Asset (
    val id:String,
    val name:String,
    val symbol:String,
    val price:String,
    val percentage: Double
)