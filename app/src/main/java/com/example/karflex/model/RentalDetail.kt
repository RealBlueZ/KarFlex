package com.example.karflex.model

data class RentalDetail (
    val rentalId: Long,
    val vehicleId: Long,
    val userId: Long,
    val modelName: String,
    val brand: String,
    val image: String,
    val duration: Int,
    val startDate: String,
    val status: String
)