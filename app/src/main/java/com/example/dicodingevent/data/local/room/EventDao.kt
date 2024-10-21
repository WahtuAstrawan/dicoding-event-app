package com.example.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dicodingevent.data.local.entity.FavoriteEventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM favoriteEvent")
    fun getAllFavoriteEvent(): LiveData<List<FavoriteEventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteEvent(event: List<FavoriteEventEntity>)

    @Query("DELETE FROM favoriteEvent WHERE id = :id")
    suspend fun deleteFavoriteEvent(id: String)
}