package com.example.karflex.database.dao

import android.content.ContentValues
import com.example.karflex.database.dbhelper.DatabaseHelper
import com.example.karflex.database.model.RentalDetail

class RentalDAO(private val dbHelper: DatabaseHelper)  {

    // buat rental baru
    fun insertRental(vehicleId: Long, userId: Long, duration: Int, startDate: String, status: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.RENTAL_VEHICLE_ID, vehicleId)
            put(DatabaseHelper.RENTAL_USERS_ID, userId)
            put(DatabaseHelper.RENTAL_DURATION, duration)
            put(DatabaseHelper.RENTAL_START_DATE, startDate)
            put(DatabaseHelper.RENTAL_STATUS, status)
        }
        return db.insert(DatabaseHelper.TABLE_RENTAL, null, values)
    }

    // rental yang sedang aktif
    fun getActiveRentalByUser(userId: Long): List<RentalDetail>{
        val list = mutableListOf<RentalDetail>()
        val db = dbHelper.readableDatabase

        val query = """SELECT r.${DatabaseHelper.RENTAL_ID}, r.${DatabaseHelper.RENTAL_VEHICLE_ID}, r.${DatabaseHelper.RENTAL_USERS_ID}, 
            |v.${DatabaseHelper.VEHICLE_MODEL},v.${DatabaseHelper.VEHICLE_BRAND}, v.${DatabaseHelper.VEHICLE_IMAGE}, 
            |r.${DatabaseHelper.RENTAL_DURATION}, r.${DatabaseHelper.RENTAL_START_DATE}, r.${DatabaseHelper.RENTAL_STATUS} 
            |FROM ${DatabaseHelper.TABLE_RENTAL} r
            |INNER JOIN ${DatabaseHelper.TABLE_VEHICLE} v ON r.${DatabaseHelper.RENTAL_VEHICLE_ID} = v.${DatabaseHelper.VEHICLE_ID}
            |WHERE r.${DatabaseHelper.RENTAL_USERS_ID} = ?
            |AND r.${DatabaseHelper.RENTAL_STATUS} = 'ACTIVE'
        """.trimMargin()

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        if(cursor.moveToFirst()){
            do {
                list.add(
                    RentalDetail(
                        rentalId = cursor.getLong(0),
                        vehicleId = cursor.getLong(1),
                        userId = cursor.getLong(2),
                        modelName = cursor.getString(3),
                        brand = cursor.getString(4),
                        image = cursor.getString(5),
                        duration = cursor.getInt(6),
                        startDate = cursor.getString(7),
                        status = cursor.getString(8)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // update durasi rental
    fun updateRentalDuration(rentalId: Long, newDuration: Int): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.RENTAL_DURATION, newDuration)
        }
        return db.update(DatabaseHelper.TABLE_RENTAL, values, "${DatabaseHelper.RENTAL_ID} = ?", arrayOf(rentalId.toString()))
    }

    // update status rental
    fun updateRentalStatus(rentalId: Long, newStatus: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.RENTAL_STATUS, newStatus)
        }

        return db.update(DatabaseHelper.TABLE_RENTAL, values, "${DatabaseHelper.RENTAL_ID} = ?", arrayOf(rentalId.toString()))
    }

    // hapus rental
    fun deleteRental(rentalId: Long): Int {
        val db = dbHelper.writableDatabase
        val values = "${DatabaseHelper.RENTAL_ID} = ?"

        return db.delete(DatabaseHelper.TABLE_RENTAL, values, arrayOf(rentalId.toString()))
    }

    //rental history
    fun getRentalHistory(userId: Long, searchQuery: String = ""): List<RentalDetail>{
        val list = mutableListOf<RentalDetail>()
        val db = dbHelper.readableDatabase

        val query = """SELECT r.${DatabaseHelper.RENTAL_ID}, r.${DatabaseHelper.RENTAL_VEHICLE_ID}, r.${DatabaseHelper.RENTAL_USERS_ID},
            |v.${DatabaseHelper.VEHICLE_MODEL}, v.${DatabaseHelper.VEHICLE_BRAND}, v.${DatabaseHelper.VEHICLE_IMAGE}
            |r.${DatabaseHelper.RENTAL_DURATION}, r.${DatabaseHelper.RENTAL_START_DATE}, r.${DatabaseHelper.RENTAL_STATUS}
            |FROM ${DatabaseHelper.TABLE_RENTAL} r
            |INNER JOIN ${DatabaseHelper.TABLE_VEHICLE} v ON r.${DatabaseHelper.RENTAL_VEHICLE_ID} = v.${DatabaseHelper.RENTAL_ID}
            |WHERE r.${DatabaseHelper.RENTAL_USERS_ID} = ?
            |AND (v.${DatabaseHelper.VEHICLE_MODEL} LIKE ? OR v.${DatabaseHelper.VEHICLE_BRAND} LIKE ?)
            |ORDER BY r.${DatabaseHelper.RENTAL_ID} DESC
        """.trimMargin()

        val searchPattern = "%$searchQuery%"
        val cursor = db.rawQuery(query, arrayOf(userId.toString(), searchPattern, searchPattern))

        if(cursor.moveToFirst()){
            do{
                list.add(RentalDetail(
                    rentalId = cursor.getLong(0),
                    vehicleId = cursor.getLong(1),
                    userId = cursor.getLong(2),
                    modelName = cursor.getString(3),
                    brand = cursor.getString(4),
                    image = cursor.getString(5),
                    duration = cursor.getInt(6),
                    startDate = cursor.getString(7),
                    status = cursor.getString(8)
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}