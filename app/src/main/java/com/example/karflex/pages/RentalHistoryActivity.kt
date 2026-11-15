package com.example.karflex.pages

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karflex.R
import com.example.karflex.adapter.RentalHistoryAdapter
import com.example.karflex.helper.BaseActivity
import com.example.karflex.adapter.adapterModel.RentalVehicle

class RentalHistoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rental_history)

        setupSharedComponents()

        val rvRentalHistory = findViewById<RecyclerView>(R.id.rvRentalHistory)

        val rentalHistoryList = listOf(
            RentalVehicle(
                R.drawable.avanza,
                "Toyota Avanza 1.3 E MT",
                "TOYOTA",
                3,
                "14 Nov 2026",
                "Active"
            ),
            RentalVehicle(
                R.drawable.brio,
                "Honda Brio Satya",
                "HONDA",
                5,
                "10 Nov 2026",
                "Active"
            ),
            RentalVehicle(
                R.drawable.yamaha,
                "XMAX 250",
                "YAMAHA",
                2,
                "05 Nov 2026",
                "Completed"
            ),
            RentalVehicle(
                R.drawable.honda,
                "Honda Supra X 125 FI",
                "HONDA",
                4,
                "28 Oct 2026",
                "Completed"
            )
        )

        rvRentalHistory.layoutManager = LinearLayoutManager(this)

        rvRentalHistory.adapter = RentalHistoryAdapter(rentalHistoryList)
    }
}