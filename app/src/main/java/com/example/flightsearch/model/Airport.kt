package com.example.flightsearch.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey
    val id : Int = 0,
    @NonNull
    @ColumnInfo(name = "iata_code")
    val iataCode : String = "",
    @NonNull
    @ColumnInfo(name = "name")
    val name : String = "",
    @NonNull
    @ColumnInfo(name = "passengers")
    val passengers : Int = 0
    )

