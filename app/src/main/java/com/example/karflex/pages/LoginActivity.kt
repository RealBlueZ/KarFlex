package com.example.karflex.pages

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.karflex.R
import com.example.karflex.database.dao.UserDAO
import com.example.karflex.database.dbhelper.DatabaseHelper
import androidx.core.content.edit
import com.example.karflex.helper.PasswordToggleHelper
import kotlin.random.Random

class LoginActivity : AppCompatActivity() {

    private lateinit var userDao: UserDAO
    private val SMS_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val dbHelper = DatabaseHelper(this)
        userDao = UserDAO(dbHelper)

        checkSmsPermission()

        val etUserLogin = findViewById<EditText>(R.id.etUserLogin)
        val etPassLogin = findViewById<EditText>(R.id.etPassLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvForgotPass = findViewById<TextView>(R.id.tvForgotPass)
        val tvRegisterHere = findViewById<TextView>(R.id.tvRegisterHere)

        val passwordToggleHelper = PasswordToggleHelper(
            etPassLogin,
            findViewById<ImageButton>(R.id.btnTogglePassword)
        )

        passwordToggleHelper.setup()

        btnLogin.setOnClickListener {
            val username = etUserLogin.text.toString().trim()
            val password = etPassLogin.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password harus diisi!", Toast.LENGTH_SHORT).show()
            }

            val cursor = userDao.loginUser(username, password)
            if(cursor != null && cursor.moveToFirst()){
                val userIdIdx = cursor.getColumnIndex(DatabaseHelper.USER_ID)
                val phoneIdx = cursor.getColumnIndex(DatabaseHelper.USER_NUMBER)

                val userId = if(userIdIdx != -1) cursor.getLong(userIdIdx) else -1L
                val number = if(phoneIdx != -1) cursor.getString(phoneIdx) else -1L
                cursor.close()

                val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                sharedPref.edit { putLong("LOGGED_IN_USER_ID", userId) }

                val otp = Random.nextInt(1000, 10000).toString()

                sendOtp(number, otp)

                val intent = Intent(this, VerifyActivity::class.java)
                intent.putExtra("OTP_CODE", otp)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()
            } else {
                cursor?.close()
                Toast.makeText(this, "Login Gagal! Username / Password Salah atau belum Terdaftar", Toast.LENGTH_SHORT).show()
            }
        }

        tvForgotPass.setOnClickListener {
            Toast.makeText(this, "Fitur Lupa Password belum tersedia", Toast.LENGTH_SHORT).show()
        }

        tvRegisterHere.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
        }
    }
    private fun sendOtp(number: Any?, otp: String) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                val smsManager = this.getSystemService(SmsManager::class.java)
                val message = "Kode OTP Anda adalah: $otp"
                smsManager.sendTextMessage(number as String?, null, message, null, null)
                Toast.makeText(this, "Kode OTP telah dikirim", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Izin SMS ditolak. OTP simulasi: $otp", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal mengirim SMS: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}