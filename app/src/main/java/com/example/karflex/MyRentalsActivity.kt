package com.example.karflex

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyRentalsActivity : AppCompatActivity() {

    private var durationDays = 3
    private var pricePerDay = 350000
    private val startCalendar = Calendar.getInstance()

    private lateinit var tvDuration: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var tvTotalPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_rentals)

        val vehicleName = intent.getStringExtra("VEHICLE_NAME") ?: "Toyota Avanza"
        pricePerDay = intent.getIntExtra("VEHICLE_PRICE", 350000)
        val vehicleImage = intent.getIntExtra("VEHICLE_IMAGE", R.drawable.avanza)

        val cardActiveRental = findViewById<CardView>(R.id.cardActiveRental)
        val tvEmptyRental = findViewById<TextView>(R.id.tvEmptyRental)
        val tvRentalVehicleName = findViewById<TextView>(R.id.tvRentalVehicleName)
        val ivRentalVehicle = findViewById<ImageView>(R.id.ivRentalVehicle)
        val tvStartDate = findViewById<TextView>(R.id.tvStartDate)
        tvDuration = findViewById(R.id.tvDuration)
        tvEndDate = findViewById(R.id.tvEndDate)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)

        val btnUpdateDuration = findViewById<Button>(R.id.btnUpdateDuration)
        val btnCancelRental = findViewById<Button>(R.id.btnCancelRental)
        val btnBackToHome = findViewById<Button>(R.id.btnBackToHome)

        tvRentalVehicleName.text = vehicleName
        ivRentalVehicle.setImageResource(vehicleImage)

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        tvStartDate.text = dateFormat.format(startCalendar.time)
        updateRentalSummary()

        btnUpdateDuration.setOnClickListener {
            val options = arrayOf("1 Day", "2 Days", "3 Days", "5 Days", "7 Days")
            val dayValues = intArrayOf(1, 2, 3, 5, 7)

            AlertDialog.Builder(this)
                .setTitle("Select New Rental Duration")
                .setItems(options) { _, which ->
                    durationDays = dayValues[which]
                    updateRentalSummary()
                    Toast.makeText(this, "Durasi diperbarui menjadi $durationDays hari", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        btnCancelRental.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cancel Rental")
                .setMessage("Apakah kamu yakin ingin membatalkan penyewaan $vehicleName?")
                .setPositiveButton("Ya, Batalkan") { _, _ ->
                    cardActiveRental.visibility = View.GONE
                    tvEmptyRental.visibility = View.VISIBLE
                    Toast.makeText(this, "Pesanan berhasil dibatalkan.", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Tidak", null)
                .show()
        }

        btnBackToHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun updateRentalSummary() {
        val endCalendar = startCalendar.clone() as Calendar
        endCalendar.add(Calendar.DAY_OF_YEAR, durationDays)
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)

        tvDuration.text = if (durationDays == 1) "1 Day" else "$durationDays Days"
        tvEndDate.text = dateFormat.format(endCalendar.time)

        val total = pricePerDay.toLong() * durationDays
        val formatter = NumberFormat.getInstance(Locale("id", "ID"))
        tvTotalPrice.text = "Rp ${formatter.format(total)}"
    }
}