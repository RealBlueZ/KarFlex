package com.example.karflex.pages

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.karflex.R
import com.example.karflex.database.dao.UserDAO
import com.example.karflex.database.dbhelper.DatabaseHelper
import com.example.karflex.helper.PasswordToggleHelper

class SignUpActivity : AppCompatActivity() {
    // menggunakan lateinit / late initialization
    // agar menghindari tipe data NULL
    private lateinit var userDao: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // database helper untuk memanggil fungsi userDAO
        val dbHelper = DatabaseHelper(this)
        userDao = UserDAO(dbHelper)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLoginHere = findViewById<TextView>(R.id.tvLoginHere)

        val passwordToggleHelper = PasswordToggleHelper(
            etPassword,
            findViewById<ImageButton>(R.id.btnTogglePassword1)
        )

        passwordToggleHelper.setup()

        val confirmPasswordToggleHelper = PasswordToggleHelper(
            etConfirmPassword,
            findViewById<ImageButton>(R.id.btnToggleConfirmPassword)
        )

        confirmPasswordToggleHelper.setup()

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val phone = etPhone.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            // validasi jika input field masih ada yang kosong
            if (username.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // validasi password minimal 6 karakter atau lebih
            else if (password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // validasi nomor telpon minimal 10 digit atau lebih
            else if (phone.length < 10){
                Toast.makeText(this, "Nomor minimal 10 angka!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (confirmPassword != password) {
                Toast.makeText(this, "Password tidak sesuai dengan sebelumnya!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // proses simpan user register ke database
            try{
                val insertId = userDao.registerUser(username, password, phone)

                // Periksa apakah SQL return positif atau negatif
                // jika 1,2,3,... maka True
                // jika -1 maka False
                if(insertId != -1L){
                    Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Register Gagal! Username sudah terdaftar.", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(this, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        tvLoginHere.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

// Menggunakan return@setOnClickListener untuk membatalkan validasi tanpa keluar dari aplikasi atau halaman