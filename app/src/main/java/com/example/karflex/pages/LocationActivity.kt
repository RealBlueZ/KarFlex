package com.example.karflex.pages

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.karflex.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//classnya blm lengkap
class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val toolBar = findViewById<Toolbar>(R.id.toolBar)

        toolBar.setNavigationOnClickListener {
            finish()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    // Nyiapin tampilan map
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val karFlexLocation = LatLng(-6.201733, 106.781592)

        val marker = MarkerOptions()
            .position(karFlexLocation)
            .title("KarFlex")
            .snippet("KarFlex Store Location")

        googleMap.addMarker(marker)

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                karFlexLocation,
                16f
            )
        )
    }
}