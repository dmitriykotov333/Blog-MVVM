package com.kotdev.blog.app.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kotdev.blog.app.models.AccountProperties
import com.kotdev.blog.app.models.AuthToken
import com.kotdev.blog.app.models.BlogPost

@Database(entities = [AuthToken::class, AccountProperties::class, BlogPost::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    abstract fun getBlogPostDao(): BlogPostDao

    companion object {
        const val DATABASE_NAME: String = "app_db"
    }


}