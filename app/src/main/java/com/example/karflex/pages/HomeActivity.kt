package com.example.karflex.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karflex.R
import com.example.karflex.model.Vehicle
import com.example.karflex.adapter.VehicleAdapter

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val rvVehicles = findViewById<RecyclerView>(R.id.rvVehicles)
        rvVehicles.layoutManager = LinearLayoutManager(this)

        val vehicleList = listOf(
            Vehicle(
                "Toyota Avanza",
                "MPV • 7 Seats • Manual",
                "Rp 350.000 / day",
                350000,
                R.drawable.avanza
            ),
            Vehicle(
                "Honda Brio",
                "City Car • 5 Seats • Automatic",
                "Rp 275.000 / day",
                275000,
                R.drawable.brio
            ),
            Vehicle(
                "Honda Scooter",
                "Automatic • 110cc",
                "Rp 100.000 / day",
                100000,
                R.drawable.honda
            ),
            Vehicle("Yamaha Sport", "Manual • 150cc", "Rp 120.000 / day", 120000, R.drawable.yamaha)
    )

        val adapter = VehicleAdapter(vehicleList)
        rvVehicles.adapter = adapter
    }
}