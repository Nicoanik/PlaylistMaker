package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entity.FavoriteTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Query("DELETE FROM favorite_tracks WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: Int?)

    @Query("SELECT * FROM favorite_tracks ORDER BY timestamp DESC")
    fun getTracks(): Flow<List<FavoriteTrackEntity>>

    @Query("SELECT trackId FROM favorite_tracks")
    suspend fun getTracksId(): List<Int>
}
