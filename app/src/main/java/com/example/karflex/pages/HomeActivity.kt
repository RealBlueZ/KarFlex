package com.example.karflex.pages

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.karflex.R
import com.example.karflex.adapter.adapterModel.Vehicle
import com.example.karflex.adapter.VehicleAdapter
import com.example.karflex.helper.BaseActivity
import org.json.JSONArray
import java.text.NumberFormat
import java.util.Locale

class HomeActivity : BaseActivity() {
    val JSON_URL = "https://gist.githubusercontent.com/axelkrnwn/01c506ac60735f553af951e550634b59/raw/08489b2ff76e1356a6df38d09948716628024195/karflex.json"

    private lateinit var rvVehicles: RecyclerView
    private val vehicleList = mutableListOf<Vehicle>()
    private lateinit var adapter: VehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_home)

        // menghilangkan bottom home bar android
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setupSharedComponents()

        // membuat layout manager recyclerview
        rvVehicles = findViewById(R.id.rvVehicles)
        rvVehicles.layoutManager = LinearLayoutManager(this)

        // mengirim data ke adapter
        adapter = VehicleAdapter(vehicleList)
        rvVehicles.adapter = adapter

        // memanggil fungsi Volley
        fetchVehicleDataFromVolley()
    }

    // fungsi untuk mengambil data JSON menggunakan Volley
    private fun fetchVehicleDataFromVolley() {
       val queue = Volley.newRequestQueue(this)

       val request = JsonArrayRequest(JSON_URL,
           {response ->
               Log.d("VOLLEY", "DATA: $response")
               parseAndDisplayVehicles(response.toString())
           },
           {error ->
               Log.e("VOLLEY", "ERROR: ${error.message}")
               Toast.makeText(this, "Gagal memuat data dari server!", Toast.LENGTH_SHORT).show()
           })
        queue.add(request)
    }

    // fungsi parsing data dan display vehicles di Home Activity
    private fun parseAndDisplayVehicles(jsonString: String){
        try{
            val jsonArray = JSONArray(jsonString)
            vehicleList.clear()

            val formatRupiah = NumberFormat.getNumberInstance(Locale("in", "ID"))

            for (i in 0 until jsonArray.length()){
                val obj = jsonArray.getJSONObject(i)

                val id = obj.optInt("id", -1)
                val name = obj.optString("model_name", "Nameless")
                val type = obj.optString("type", "Car or Motorcycle")
                val transmission = obj.optString("transmission", "Automatic")
                val pricePerDay = obj.optInt("price_per_day", 0)

                val imageUrl = obj.optString("image_url", "")

                val subtitle = "$type • $transmission"
                val priceDisplay = "Rp ${formatRupiah.format((pricePerDay))} / day"

                // memasukan data JSON agar dapat ditampikan oleh RecyclerView
                vehicleList.add(
                    Vehicle(
                        id = id,
                        name = name,
                        type = subtitle,
                        priceDisplay = priceDisplay,
                        pricePerDay = pricePerDay,
                        imageUrl = imageUrl
                    )
                )
            }

            // memanggil adapter untuk merubah data saat ini
            adapter.notifyDataSetChanged()

            // debug jika data sudah terkirim ke fungsi parsing
            Log.d("PARSE", "DATA: ${jsonArray.length()}")

        } catch (e: Exception){
            Toast.makeText(this, "Gagal memprosen JSON!", Toast.LENGTH_SHORT).show()
        }
    }
}