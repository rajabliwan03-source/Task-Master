package com.example.taskmaster

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * DatabaseHelper: Manages the local SQLite database for Taskmaster.
 * Handles schema creation and incremental migrations.
 */
class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "taskmaster.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_TASKS = "tasks"

        // Column Names
        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_DESC = "description"
        const val COL_IMAGE_PATH = "image_path"
        const val COL_LATITUDE = "latitude"
        const val COL_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create table with all columns for version 2
        val createTableQuery = """
            CREATE TABLE $TABLE_TASKS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT NOT NULL,
                $COL_DESC TEXT,
                $COL_IMAGE_PATH TEXT,
                $COL_LATITUDE REAL,
                $COL_LONGITUDE REAL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Feature Integration: Append new columns to existing version 1 schema
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_TASKS ADD COLUMN $COL_DESC TEXT")
            db.execSQL("ALTER TABLE $TABLE_TASKS ADD COLUMN $COL_IMAGE_PATH TEXT")
            db.execSQL("ALTER TABLE $TABLE_TASKS ADD COLUMN $COL_LATITUDE REAL")
            db.execSQL("ALTER TABLE $TABLE_TASKS ADD COLUMN $COL_LONGITUDE REAL")
        }
    }

    /**
     * Securely inserts a new task into the SQLite database.
     */
    fun insertTask(title: String, desc: String?, imagePath: String?, lat: Double, lon: Double): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_DESC, desc)
            put(COL_IMAGE_PATH, imagePath)
            put(COL_LATITUDE, lat)
            put(COL_LONGITUDE, lon)
        }
        return db.insert(TABLE_TASKS, null, contentValues)
    }

    /**
     * Helper: Saves Bitmap to Internal Storage and returns the path string
     */
    fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val fileName = "task_${UUID.randomUUID()}.jpg"
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file.absolutePath
    }
}
