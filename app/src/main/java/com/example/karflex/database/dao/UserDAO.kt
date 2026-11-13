package com.example.karflex.database.dao

import android.content.ContentValues
import android.database.Cursor
import com.example.karflex.database.DatabaseHelper
import com.example.karflex.model.UserProfile

class UserDAO(private val dbHelper: DatabaseHelper) {
    fun registerUser(username: String, password: String, number: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.USER_NAME, username)
            put(DatabaseHelper.USER_PASSWORD, password)
            put(DatabaseHelper.USER_NUMBER, number)
        }
        return db.insert(DatabaseHelper.TABLE_USERS, null, values)
    }

    fun loginUser(name: String, password: String): Cursor? {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.USER_NAME} = ? AND ${DatabaseHelper.USER_PASSWORD} =?"
        return db.rawQuery(query, arrayOf(name, password))
    }

    fun getUserProfile(userId: Long): UserProfile? {
        val db = dbHelper.readableDatabase
        val query = """ SELECT ${DatabaseHelper.USER_NAME}, ${DatabaseHelper.USER_NUMBER}
            |FROM ${DatabaseHelper.TABLE_USERS}
            |WHERE ${DatabaseHelper.USER_ID} = ?
        """.trimMargin()

        val cursor: Cursor = db.rawQuery(query, arrayOf(userId.toString()))
        var userProfile: UserProfile? = null

        if(cursor.moveToFirst()){
            val username = cursor.getString(0)
            val number = cursor.getString(1)
            userProfile = UserProfile(username, number)
        }

        cursor.close()
        return userProfile
    }
}