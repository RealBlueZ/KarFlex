package com.example.karflex.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "karflex.db"
        private const val DATABASE_VERSION = 1

        // buat nama tabel
        const val TABLE_USERS = "Users"
        const val TABLE_RENTAL = "Rentals"
        const val TABLE_VEHICLE = "Vehicle"

        // entity untuk users
        const val USER_ID = "UserID"
        const val USER_NAME = "Username"
        const val USER_PASSWORD = "Password"
        const val USER_NUMBER = "PhoneNumber"

        // entity untuk rental
        const val RENTAL_ID = "RentalID"
        const val RENTAL_VEHICLE_ID = "VehicleID" // foreign key utk table vehicle
        const val RENTAL_USERS_ID = "UserID" // foreign key utk table users
        const val RENTAL_DURATION = "Duration"
        const val RENTAL_START_DATE= "StartDate"
        const val RENTAL_STATUS = "Status"

        // entity untuk vehicle
        const val VEHICLE_ID = "VehicleID"
        const val VEHICLE_MODEL = "ModelName"
        const val VEHICLE_TYPE = "Type"
        const val VEHICLE_BRAND = "Brand"
        const val VEHICLE_PRICE = "PricePerDay"
        const val VEHICLE_TRANSMISSION = "Transmission"
        const val VEHICLE_IMAGE = "Image"
        const val VEHICLE_DESC = "Description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_key = ON;")

        val createTableUser = """ CREATE TABLE IF NOT EXISTS $TABLE_USERS (
            |$USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            |$USER_NAME TEXT NOT NULL UNIQUE,
            |$USER_PASSWORD TEXT NOT NULL,
            |$USER_NUMBER TEXT NOT NULL)
        """.trimMargin()

        val createTableRental = """ CREATE TABLE IF NOT EXISTS $TABLE_RENTAL (
            |$RENTAL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            |$RENTAL_USERS_ID INTEGER NOT NULL,
            |$RENTAL_VEHICLE_ID INTEGER NOT NULL,
            |$RENTAL_DURATION INTEGER NOT NULL,
            |$RENTAL_START_DATE TEXT NOT NULL,
            |$RENTAL_STATUS TEXT NOT NULL,
            |FOREIGN KEY ($RENTAL_USERS_ID) REFERENCES $TABLE_USERS($USER_ID),
            |FOREIGN KEY ($RENTAL_VEHICLE_ID) REFERENCES $TABLE_VEHICLE($VEHICLE_ID) )
        """.trimMargin()

        val createTableVehicle = """ CREATE TABLE IF NOT EXISTS $TABLE_VEHICLE (
            |$VEHICLE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            |$VEHICLE_MODEL TEXT NOT NULL,
            |$VEHICLE_TYPE TEXT NOT NULL,
            |$VEHICLE_BRAND TEXT NOT NULL,
            |$VEHICLE_PRICE INTEGER NOT NULL,
            |$VEHICLE_TRANSMISSION TEXT NOT NULL,
            |$VEHICLE_IMAGE TEXT NOT NULL,
            |$VEHICLE_DESC TEXT NOT NULL )
        """.trimMargin()

        db?.execSQL(createTableUser)
        db?.execSQL(createTableRental)
        db?.execSQL(createTableVehicle)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_RENTAL")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_VEHICLE")
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase?){
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }
}