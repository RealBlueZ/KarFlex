package com.example.karflex.model

data class RentalVehicle(
    val vehicleImage: Int,
    val vehicleName: String,
    val vehicleBrand: String,
    val durationRent: Int,
    val startDateRent: Int,
    val statusRent: String
)
