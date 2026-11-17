package com.example.karflex.helper

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.karflex.R
import com.example.karflex.pages.HomeActivity
import com.example.karflex.pages.MyRentalsActivity
import com.example.karflex.pages.ProfileActivity

open class BaseActivity: AppCompatActivity() {

    protected fun setupAppBar() {
        val llNameApp = findViewById<LinearLayout>(R.id.llNameApp)
        val btnNotification = findViewById<ImageButton>(R.id.btnNotifications)
        val ivProfile = findViewById<ImageView>(R.id.ivPhotoProfile)

        llNameApp.setOnClickListener {
            if(this !is HomeActivity){
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

        btnNotification.setOnClickListener {
            Toast.makeText(this, "Notification clicked!", Toast.LENGTH_SHORT).show()
        }

        ivProfile.setOnClickListener {
            if(this !is ProfileActivity){
                startActivity(Intent(this, ProfileActivity::class.java))
            }
        }
    }

    protected fun setupBottomNavigation() {
        val btnBrowse = findViewById<ImageButton>(R.id.btnBrowse)
        val btnMyRental = findViewById<ImageButton>(R.id.btnMyRental)
        val btnProfile = findViewById<ImageButton>(R.id.btnProfile)

        btnBrowse.setOnClickListener {
            if(this !is HomeActivity){
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

        btnMyRental.setOnClickListener {
            if (this !is MyRentalsActivity) {
                startActivity(Intent(this, MyRentalsActivity::class.java))
            }
        }

        btnProfile.setOnClickListener {
            if (this !is ProfileActivity) {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
        }

    }

    protected fun setupSharedComponents() {
        setupAppBar()
        setupBottomNavigation()
    }
}