package com.example.karflex.pages

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.karflex.R
import com.example.karflex.helper.BaseActivity

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupSharedComponents()

        val llRentalHistory = findViewById<LinearLayout>(R.id.llRentalHistory)
        val llStoreLocation = findViewById<LinearLayout>(R.id.llStoreLocation)

        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // AMBIL DARI LOGIN ACTIVITY
        val username = intent.getStringExtra("username")
        val phone = intent.getStringExtra("phone")

        val tvPhone = findViewById<TextView>(R.id.tvPhoneNumber)
        val tvUsername = findViewById<TextView>(R.id.tvUsername)

        tvPhone.text = phone
        tvUsername.text = username

        llRentalHistory.setOnClickListener {
            val intent = Intent(this, RentalHistoryActivity::class.java)
            startActivity(intent)
        }

        llStoreLocation.setOnClickListener {
            val intent = Intent(this, StoreLocationActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Logout"){ _, _ ->
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }
}