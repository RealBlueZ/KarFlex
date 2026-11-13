package com.example.karflex.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "KarFlex.db"
        private const val DATABASE_VERSION = 1

        // buat nama tabel
        const val TABLE_USERS = "users"
        const val TABLE_RENTAL = "rental"
        const val TABLE_VEHICLE = "vehicle"

        // entity untuk users
        const val COLUMN_USER_ID = "UserID"
        const val COLUMN_USER_NAME = "Username"
        const val COLUMN_USER_PASSWORD = "Password"
        const val COLUMN_USER_NUMBER = "PhoneNumber"

        // entity untuk rental
        const val COLUMN_RENTAL_ID = "RentalID"
        const val COLUMN_RENTAL_VEHICLE_ID = "VehicleID" // foreign key utk table vehicle
        const val COLUMN_RENTAL_USERS_ID = "UserID" // foreign key utk table users
        const val COLUMN_RENTAL_DURATION = "Duration"
        const val COLUMN_RENTAL_START_DATE= "StartDate"
        const val COLUMN_RENTAL_STATUS = "Status"

        // entity untuk vehicle
        const val COLUMN_VEHICLE_ID = "VehicleID"
        const val COLUMN_VEHICLE_MODEL = "ModelName"
        const val COLUMN_VEHICLE_TYPE = "Type"
        const val COLUMN_VEHICLE_BRAND = "Brand"
        const val COLUMN_VEHICLE_PRICE = "PricePerDay"
        const val COLUMN_VEHICLE_TRANSMISSION = "Transmission"
        const val COLUMN_VEHICLE_IMAGE = "Image"
        const val COLUMN_VEHICLE_DESC = "Description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_key = ON;")

        val createTableUser = """ CREATE TABLE IF NOT EXISTS $TABLE_USERS (
            |$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            |$COLUMN_USER_NAME TEXT NOT NULL UNIQUE,
            |$COLUMN_USER_PASSWORD TEXT NOT NULL,
            |$COLUMN_USER_NUMBER TEXT )
        """.trimMargin()

        val createTableRental = """ CREATE TABLE IF NOT EXISTS $TABLE_RENTAL (
            |$COLUMN_RENTAL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            |$COLUMN_RENTAL_USERS_ID INTEGER NOT NULL,
            |$COLUMN_RENTAL_VEHICLE_ID INTEGER NOT NULL,
            |$COLUMN_RENTAL_DURATION INTEGER NOT NULL,
            |$COLUMN_RENTAL_START_DATE TEXT NOT NULL,
            |$COLUMN_RENTAL_STATUS TEXT NOT NOT NULL 
            |FOREIGN KEY ($COLUMN_RENTAL_USERS_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
            |FOREIGN KEY ($COLUMN_RENTAL_VEHICLE_ID) REFERENCES $TABLE_VEHICLE($COLUMN_VEHICLE_ID) )
        """.trimMargin()

        val createTableVehicle = """ CREATE TABLE IF NOT EXISTS $TABLE_VEHICLE (
            |$COLUMN_VEHICLE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            |$COLUMN_VEHICLE_MODEL TEXT NOT NULL,
            |$COLUMN_VEHICLE_TYPE TEXT NOT NULL,
            |$COLUMN_VEHICLE_BRAND TEXT NOT NULL,
            |$COLUMN_VEHICLE_PRICE INTEGER NOT NULL,
            |$COLUMN_VEHICLE_TRANSMISSION TEXT NOT NULL,
            |$COLUMN_VEHICLE_IMAGE TEXT NOT NULL,
            |$COLUMN_VEHICLE_DESC TEXT NOT NULL )
        """.trimMargin()

        db?.execSQL(createTableUser)
        db?.execSQL(createTableRental)
        db?.execSQL(createTableVehicle)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_RENTAL")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_VEHICLE")
    }

    override fun onConfigure(db: SQLiteDatabase){
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }
}