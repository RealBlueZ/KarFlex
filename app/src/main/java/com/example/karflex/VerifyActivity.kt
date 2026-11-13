package com.example.karflex

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VerifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        val vehicleName = intent.getStringExtra("VEHICLE_NAME") ?: "Toyota Avanza"
        val vehiclePrice = intent.getIntExtra("VEHICLE_PRICE", 350000)
        val vehicleImage = intent.getIntExtra("VEHICLE_IMAGE", R.drawable.avanza)

        val btnBack = findViewById<TextView>(R.id.btnBackVerify)
        val etOtp1 = findViewById<EditText>(R.id.etOtp1)
        val etOtp2 = findViewById<EditText>(R.id.etOtp2)
        val etOtp3 = findViewById<EditText>(R.id.etOtp3)
        val etOtp4 = findViewById<EditText>(R.id.etOtp4)
        val tvResend = findViewById<TextView>(R.id.tvResend)
        val btnVerify = findViewById<Button>(R.id.btnVerifyContinue)

        setupOtpAutoMove(etOtp1, etOtp2)
        setupOtpAutoMove(etOtp2, etOtp3)
        setupOtpAutoMove(etOtp3, etOtp4)

        btnBack.setOnClickListener { finish() }

        tvResend.setOnClickListener {
            Toast.makeText(this, "Kode OTP baru telah dikirim!", Toast.LENGTH_SHORT).show()
        }

        btnVerify.setOnClickListener {
            val code = "${etOtp1.text}${etOtp2.text}${etOtp3.text}${etOtp4.text}"
            if (code.length < 4) {
                Toast.makeText(this, "Masukkan 4 digit kode verifikasi!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Verifikasi Berhasil! Pesanan dibuat.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MyRentalsActivity::class.java).apply {
                    putExtra("VEHICLE_NAME", vehicleName)
                    putExtra("VEHICLE_PRICE", vehiclePrice)
                    putExtra("VEHICLE_IMAGE", vehicleImage)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupOtpAutoMove(current: EditText, next: EditText) {
        current.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) next.requestFocus()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}