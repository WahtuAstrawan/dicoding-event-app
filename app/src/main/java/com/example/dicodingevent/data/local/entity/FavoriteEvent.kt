package com.example.dicodingevent.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dicodingevent.ui.adapter.EventItem

@Entity(tableName = "favoriteEvent")
data class FavoriteEventEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = false)
    override val id: Int,

    @field:ColumnInfo(name = "mediaCover")
    override val mediaCover: String,

    @field:ColumnInfo(name = "name")
    override val name: String,

    @field:ColumnInfo(name = "beginTime")
    override val beginTime: String,

    @field:ColumnInfo(name = "summary")
    override val summary: String,
): EventItem