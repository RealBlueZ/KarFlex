package com.example.karflex.pages

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.karflex.R
import com.example.karflex.helper.BaseActivity

class DetailVehicleActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_detail_vehicle)

        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setupSharedComponents()

        val ivVehicle = findViewById<ImageView>(R.id.ivVehicle)
        val tvVehicleName = findViewById<TextView>(R.id.tvVehicleName)
        val tvVehicleBrand = findViewById<TextView>(R.id.tvVehicleBrand)
        val tvVehicleTransmission = findViewById<TextView>(R.id.tvTransmission)
        val tvVehicleType = findViewById<TextView>(R.id.tvVehicleType)
        val tvAvailability = findViewById<TextView>(R.id.tvAvailability)
        val tvVehiclePrice = findViewById<TextView>(R.id.tvVehiclePrice)
        val tvVehicleDesc = findViewById<TextView>(R.id.tvVehicleDescription)

        val etRentalDuration = findViewById<EditText>(R.id.etRentalDuration)
        val btnRent = findViewById<Button>(R.id.btnRent)

        val llRentalForm = findViewById<LinearLayout>(R.id.layoutRentalForm)
        val llRentedOut = findViewById<LinearLayout>(R.id.layoutRentedOut)


        val vehicleImage = intent.getStringExtra("VEHICLE_IMAGE") ?: ""
        val vehicleName = intent.getStringExtra("vehicle_name")
        val vehicleBrand = intent.getStringExtra("vehicle_brand")
        val transmission = intent.getStringExtra("vehicle_transmission")
        val vehicleType = intent.getStringExtra("vehicle_type")
        val vehiclePrice = intent.getStringExtra("vehicle_price")
        val description = intent.getStringExtra("vehicle_description")
        val isRented = intent.getBooleanExtra("is_rented", false)


//        ivVehicle.setImageResource(vehicleImage)
        tvVehicleName.text = vehicleName
        tvVehicleBrand.text = vehicleBrand
        tvVehicleTransmission.text = transmission
        tvVehicleType.text = vehicleType
        tvVehiclePrice.text = vehiclePrice
        tvVehicleDesc.text = description

        if(isRented){
            tvAvailability.text = "Rented Out"
            tvAvailability.setTextColor(
                getColor(android.R.color.holo_red_dark)
            )

            llRentalForm.visibility = View.GONE
            llRentedOut.visibility = View.VISIBLE
        }else{
            tvAvailability.text = "Available"
            tvAvailability.setTextColor(
                getColor(android.R.color.holo_green_dark)
            )

            llRentalForm.visibility = View.VISIBLE
            llRentedOut.visibility = View.GONE
        }

        btnRent.setOnClickListener {

            val durationInput = etRentalDuration.text.toString().trim()

            if(durationInput.isEmpty()){
                etRentalDuration.error = "Rental duration is required"
                Toast.makeText(this, "Please enter rental duration", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            val duration = durationInput.toIntOrNull()

            if(duration == null){
                Toast.makeText(this, "Rental duration must be a number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            else if(duration <= 0){
                Toast.makeText(this, "Rental duration must be more than 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Rental successful! Duration: $duration days", Toast.LENGTH_SHORT).show()


            // LANJUT BUAT SIMPAN KE SQLite
        }

    }
}