package com.example.karflex.pages

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.karflex.R
import com.example.karflex.database.dao.UserDAO
import com.example.karflex.database.dbhelper.DatabaseHelper
import com.example.karflex.helper.BaseActivity

class ProfileActivity : BaseActivity() {

    private lateinit var userDao: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_profile)

        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val dbHelper = DatabaseHelper(this)
        userDao = UserDAO(dbHelper)

        setupSharedComponents()

        val llRentalHistory = findViewById<LinearLayout>(R.id.llRentalHistory)
        val llStoreLocation = findViewById<LinearLayout>(R.id.llStoreLocation)

        val tvPhone = findViewById<TextView>(R.id.tvPhoneNumber)
        val tvUsername = findViewById<TextView>(R.id.tvUsername)

        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Ambil data dari database
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val sessionUserId = sharedPref.getLong("LOGGED_IN_USER_ID", -1L)
        val userId = if(sessionUserId != -1L){
            sessionUserId
        } else {
            intent.getLongExtra("USER_ID", -1L)
        }
        // Ambil nama & nomor hp
        if(userId != -1L){
            val profile = userDao.getUserProfile(userId)
            if(profile != null){
                tvUsername.text = if(profile.username.isNotBlank()) profile.username else "User"

                val numberExist = profile.number?.trim()
                if(numberExist.isNullOrEmpty()){
                    tvPhone.text = ""
                    tvPhone.hint = "Nomor tidak dicantumkan"
                } else {
                    tvPhone.text = numberExist
                }
            }
        } else{
            // fallback jika fungsi utama gagal
            val username = intent.getStringExtra("username")
            val phone = intent.getStringExtra("phoneNumber")

            tvPhone.text = phone
            tvUsername.text = username
        }

        llRentalHistory.setOnClickListener {
            val intent = Intent(this, RentalHistoryActivity::class.java)
            startActivity(intent)
        }

        llStoreLocation.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    // function untuk meminta konfirmasi user ketika ingin melakukan logout
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Logout"){ _, _ ->
                // hapus sesi user saat ini
                val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                sharedPref.edit().remove("LOGGED_IN_USER_ID").apply()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }
}