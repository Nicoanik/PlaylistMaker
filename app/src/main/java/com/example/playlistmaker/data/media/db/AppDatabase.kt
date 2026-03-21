package com.example.playlistmaker.data.media.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.media.db.dao.FavoriteTrackDao
import com.example.playlistmaker.data.media.db.dao.PlaylistDao
import com.example.playlistmaker.data.media.db.dao.PlaylistTrackDao
import com.example.playlistmaker.data.media.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.data.media.db.entity.PlaylistEntity
import com.example.playlistmaker.data.media.db.entity.PlaylistTrackEntity

@Database(
    version = 1,
    entities = [
        FavoriteTrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteTrackDao(): FavoriteTrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistTrackDao(): PlaylistTrackDao
}
