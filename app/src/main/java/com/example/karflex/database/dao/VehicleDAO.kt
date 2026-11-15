package com.example.karflex.database.dao

import android.content.ContentValues
import android.database.Cursor
import com.example.karflex.database.dbhelper.DatabaseHelper

class VehicleDAO (private val dbHelper: DatabaseHelper) {

    fun insertVehicle(modelName: String, type: String, brand: String, pricePerDay: Int, transmission: String, image: String, desc: String): Long{
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.VEHICLE_MODEL, modelName)
            put(DatabaseHelper.VEHICLE_TYPE, type)
            put(DatabaseHelper.VEHICLE_BRAND, brand)
            put(DatabaseHelper.VEHICLE_PRICE, pricePerDay)
            put(DatabaseHelper.VEHICLE_TRANSMISSION, transmission)
            put(DatabaseHelper.VEHICLE_IMAGE, image)
            put(DatabaseHelper.VEHICLE_DESC, desc)
        }
        return db.insert(DatabaseHelper.TABLE_VEHICLE, null, values)
    }

    fun getVehicleById(vehicleId: Long): Cursor? {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_VEHICLE} WHERE ${DatabaseHelper.VEHICLE_ID} = ?"

        return db.rawQuery(query, arrayOf(vehicleId.toString()))

    }

    fun getAllVehicle(): Cursor? {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_VEHICLE}"

        return db.rawQuery(query, null)
    }
}