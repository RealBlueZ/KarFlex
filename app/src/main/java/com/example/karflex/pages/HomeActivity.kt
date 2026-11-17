package com.example.karflex.pages

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karflex.R
import com.example.karflex.adapter.adapterModel.Vehicle
import com.example.karflex.adapter.VehicleAdapter
import com.example.karflex.helper.BaseActivity
import com.example.karflex.services.ACTION_VEHICLE_DATA
import com.example.karflex.services.EXTRA_JSON_STRING
import com.example.karflex.services.JSONService
import org.json.JSONArray
import java.text.NumberFormat
import java.util.Locale

class HomeActivity : BaseActivity() {

    private lateinit var rvVehicles: RecyclerView
    private val vehicleList = mutableListOf<Vehicle>()
    private lateinit var adapter: VehicleAdapter

    private val vehicleReciever = object: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
           if(intent?.action == ACTION_VEHICLE_DATA){
               val jsonString = intent.getStringExtra(EXTRA_JSON_STRING)
               if(!jsonString.isNullOrEmpty()){
                   parseAndDisplayVehicles(jsonString)
               }
           }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_home)

        // menghilangkan bottom action bar android
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setupSharedComponents()

        rvVehicles = findViewById(R.id.rvVehicles)
        rvVehicles.layoutManager = LinearLayoutManager(this)

        val adapter = VehicleAdapter(vehicleList)
        rvVehicles.adapter = adapter

        val serviceIntent = Intent(this, JSONService::class.java)
        startService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter(ACTION_VEHICLE_DATA)
        ContextCompat.registerReceiver(this, vehicleReciever, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(vehicleReciever)
    }

    private fun parseAndDisplayVehicles(jsonString: String){
        try{
            val jsonArray = JSONArray(jsonString)
            vehicleList.clear()

            val formatRupiah = NumberFormat.getNumberInstance(Locale("in", "ID"))

            for (i in 0 until jsonArray.length()){
                val obj = jsonArray.getJSONObject(i)

                val name = obj.optString("name", "Nameless")
                val type = obj.optString("type", "Car or Motorcycle")
                val transmission = obj.optString("transmission", "Automatic")
                val pricePerDay = obj.optInt("Price", 0)
                val imageName = obj.optString("image", "canvas")

                val imageResId = resources.getIdentifier(imageName, "drawable", packageName).takeIf { it != 0 } ?: R.drawable.none_image

                val subtitle = "$type • $transmission"
                val priceDisplay = "Rp ${formatRupiah.format((pricePerDay))} / day"

                vehicleList.add(
                    Vehicle(
                        name = name,
                        type = subtitle,
                        priceDisplay = priceDisplay,
                        pricePerDay = pricePerDay,
                        imageResId = imageResId
                    )
                )
            }

            adapter.notifyDataSetChanged()

        } catch (e: Exception){
            Toast.makeText(this, "failed to load data!", Toast.LENGTH_SHORT).show()
        }
    }
}