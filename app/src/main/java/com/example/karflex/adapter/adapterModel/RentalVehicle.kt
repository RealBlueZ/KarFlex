package com.example.karflex.adapter.adapterModel

data class RentalVehicle(
    val vehicleImage: Int,
    val vehicleName: String,
    val vehicleBrand: String,
    val durationRent: Int,
    val startDateRent: String,
    val statusRent: String
)