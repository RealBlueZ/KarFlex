package com.example.karflex.adapter.adapterModel

data class MyRentalVehicles(
    val rentalId: Long,
    val vehicleImage: String,
    val vehicleName: String,
    val vehicleBrand: String,
    val durationRent: Int,
    val startDateRent: String,
    val statusRent: String,
    val endDateRent: String
)
