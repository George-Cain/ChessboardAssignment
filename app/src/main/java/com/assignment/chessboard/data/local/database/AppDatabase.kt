package com.assignment.chessboard.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.assignment.chessboard.data.local.dao.KnightPathDao
import com.assignment.chessboard.data.local.entity.KnightPath

@Database(entities = [KnightPath::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun knightPathDao(): KnightPathDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "knight_path_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
