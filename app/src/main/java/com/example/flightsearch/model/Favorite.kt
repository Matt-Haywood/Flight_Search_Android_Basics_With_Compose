package com.example.flightsearch.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @NonNull
    @ColumnInfo(name = "departure_code")
    val departureCode : String,
    @NonNull
    @ColumnInfo(name = "destination_code")
    val destinationCode : String
)