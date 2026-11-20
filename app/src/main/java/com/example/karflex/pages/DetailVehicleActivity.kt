package com.example.karflex.pages

import android.os.Bundle
import android.util.Log
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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.karflex.R
import com.example.karflex.helper.BaseActivity

class DetailVehicleActivity : BaseActivity() {

    private val JSON_URL =
        "https://gist.githubusercontent.com/axelkrnwn/01c506ac60735f553af951e550634b59/raw/08489b2ff76e1356a6df38d09948716628024195/karflex.json"

    lateinit var ivVehicle: ImageView
    lateinit var tvVehicleName: TextView
    lateinit var tvVehicleBrand: TextView
    lateinit var tvVehicleTransmission: TextView
    lateinit var tvVehicleType: TextView
    lateinit var tvAvailability: TextView
    lateinit var tvVehiclePrice: TextView
    lateinit var tvVehicleDesc: TextView
    lateinit var etRentalDuration: EditText
    lateinit var btnRent: Button
    lateinit var llRentalForm: LinearLayout
    lateinit var llRentedOut: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_vehicle)

        WindowCompat.setDecorFitsSystemWindows(window, false)


        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setupSharedComponents()

        ivVehicle = findViewById(R.id.ivVehicle)
        tvVehicleName = findViewById(R.id.tvVehicleName)
        tvVehicleBrand = findViewById(R.id.tvVehicleBrand)
        tvVehicleTransmission = findViewById(R.id.tvTransmission)
        tvVehicleType = findViewById(R.id.tvVehicleType)
        tvAvailability = findViewById(R.id.tvAvailability)
        tvVehiclePrice = findViewById(R.id.tvVehiclePrice)
        tvVehicleDesc = findViewById(R.id.tvVehicleDescription)

        etRentalDuration = findViewById(R.id.etRentalDuration)
        btnRent = findViewById(R.id.btnRentDetailActivity)

        llRentalForm = findViewById(R.id.layoutRentalForm)
        llRentedOut = findViewById(R.id.layoutRentedOut)


        val vehicleId = intent.getIntExtra("VEHICLE_ID", -1)

        if(vehicleId != -1){
            loadVehicleDetail(vehicleId)
        }else{
            Toast.makeText(this, "Vehicle ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        val isRented = intent.getBooleanExtra("is_rented", false)

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
    private fun loadVehicleDetail(vehicleId: Int) {

        val queue = Volley.newRequestQueue(this)

        val request = JsonArrayRequest(
            JSON_URL,
            { response ->

                var vehicleFound = false

                for (i in 0 until response.length()) {

                    val obj = response.getJSONObject(i)

                    if (obj.optInt("id") == vehicleId) {

                        vehicleFound = true

                        val name = obj.optString("model_name")
                        val type = obj.optString("type")
                        val brand = obj.optString("brand")
                        val price = obj.optInt("price_per_day")
                        val transmission = obj.optString("transmission")
                        val image = obj.optString("image_url")
                        val description = obj.optString("description")

                        tvVehicleName.text = name
                        tvVehicleBrand.text = brand
                        tvVehicleType.text = type
                        tvVehicleTransmission.text = transmission
                        tvVehiclePrice.text = "Rp $price"
                        tvVehicleDesc.text = description

                        Glide.with(this)
                            .load(image)
                            .placeholder(R.drawable.none_image)
                            .error(R.drawable.none_image)
                            .into(ivVehicle)

                        break
                    }
                }

                if (!vehicleFound) {
                    Toast.makeText(
                        this,
                        "Vehicle not found",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            },
            { error ->
                Log.e("VEHICLE_DETAIL", "Error: ${error.message}")

                Toast.makeText(
                    this,
                    "Failed to load vehicle details",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        queue.add(request)
    }
}