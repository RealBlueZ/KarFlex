package com.example.karflex.pages

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.karflex.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUserLogin = findViewById<EditText>(R.id.etUserLogin)
        val etPassLogin = findViewById<EditText>(R.id.etPassLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvForgotPass = findViewById<TextView>(R.id.tvForgotPass)
        val tvRegisterHere = findViewById<TextView>(R.id.tvRegisterHere)

        btnLogin.setOnClickListener {
            val usernameOrEmail = etUserLogin.text.toString().trim()
            val password = etPassLogin.text.toString().trim()

            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username/Email dan Password harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login Berhasil! Selamat datang kembali.", Toast.LENGTH_SHORT).show()
            }
        }

        tvForgotPass.setOnClickListener {
            Toast.makeText(this, "Fitur Lupa Password belum tersedia", Toast.LENGTH_SHORT).show()
        }
        tvRegisterHere.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}