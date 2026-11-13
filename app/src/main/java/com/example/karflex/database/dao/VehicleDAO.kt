package com.example.karflex.database.dao

import android.database.Cursor
import com.example.karflex.database.DatabaseHelper

class VehicleDAO (private val dbHelper: DatabaseHelper) {
    fun getVehicleById(vehicleId: Long): Cursor? {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_VEHICLE} WHERE ${DatabaseHelper.COLUMN_VEHICLE_ID} = ?"

        return db.rawQuery(query, arrayOf(vehicleId.toString()))

    }
}