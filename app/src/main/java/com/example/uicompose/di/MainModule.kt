package com.example.uicompose.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uicompose.data.db.UsersDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(
    SingletonComponent::class
)
class MainModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): UsersDB{
        return Room.databaseBuilder(
            context,
            UsersDB::class.java,
            "USERS_DATABASE"
        ).build()
    }
}