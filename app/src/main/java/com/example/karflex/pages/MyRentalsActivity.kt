
package com.example.karflex.pages

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karflex.R
import com.example.karflex.adapter.MyRentalAdapter
import com.example.karflex.adapter.adapterModel.MyRentalVehicles
import com.example.karflex.helper.BaseActivity
import com.example.karflex.database.dao.RentalDAO
import com.example.karflex.database.dbhelper.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Class yang mengurus page my rentals, yaitu page yang menampilkan vehicle apa saja yang sedang di rental user
class MyRentalsActivity : BaseActivity() {

    private lateinit var rvMyRentals: RecyclerView
    private lateinit var tvEmptyRental: TextView
    private lateinit var rentalAdapter: MyRentalAdapter

    private val mtRentalList = mutableListOf<MyRentalVehicles>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_my_rentals)

        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setupSharedComponents()

        rvMyRentals = findViewById(R.id.rvMyRentals)
        tvEmptyRental = findViewById(R.id.tvEmptyRental)

        val btnBackToHome = findViewById<Button>(R.id.btnBackToHome)

        rvMyRentals.layoutManager = LinearLayoutManager(this)

        rentalAdapter = MyRentalAdapter(
            mtRentalList,

            onUpdateClick = { rental ->
                showUpdateDialog(rental)
            },

            onCancelClick = { rental ->
                showCancelDialog(rental)
            }
        )

        rvMyRentals.adapter = rentalAdapter

        loadMyRentals()

        btnBackToHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    // Ngambil list kendaraan yang sedang di rental oleh user yang sudah log in
    private fun loadMyRentals() {

        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        val userId = sharedPref.getLong("LOGGED_IN_USER_ID", -1L)

        if (userId == -1L) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            return
        }

        val databaseHelper = DatabaseHelper(this)
        val rentalDAO = RentalDAO(databaseHelper)

        val rentals = rentalDAO.getActiveRentalByUser(userId)

        mtRentalList.clear()

        for (rental in rentals) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val calendar = Calendar.getInstance()
            calendar.time = dateFormat.parse(rental.startDate)!!

            calendar.add(Calendar.DAY_OF_MONTH, rental.duration)

            val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

            val myRental = MyRentalVehicles(
                rentalId = rental.rentalId,
                vehicleImage = rental.image,
                vehicleName = rental.modelName,
                vehicleBrand = rental.brand,
                durationRent = rental.duration,
                startDateRent = rental.startDate,
                statusRent = rental.status,
                endDateRent = endDate
            )

            mtRentalList.add(myRental)
        }

        updateRentalList()
    }

    // function untuk update ui berdasarkan isi list
    private fun updateRentalList() {
        rentalAdapter.notifyDataSetChanged()

        if (mtRentalList.isEmpty()) {
            rvMyRentals.visibility = View.GONE
            tvEmptyRental.visibility = View.VISIBLE
        } else {
            rvMyRentals.visibility = View.VISIBLE
            tvEmptyRental.visibility = View.GONE
        }
    }

    // function yang bertugas untuk mendapatkan angka durasi baru dari user yang berupa Alert Dialog
    private fun showUpdateDialog(rental: MyRentalVehicles) {

        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = "Enter duration in days"
            setText(rental.durationRent.toString())
            setSelectAllOnFocus(true)
        }

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 8, 48, 0)
            addView(input)
        }

        AlertDialog.Builder(this)
            .setTitle("Update Rental Duration")
            .setMessage("Input duration in days")
            .setView(container)
            .setPositiveButton("UPDATE", null)
            .setNegativeButton("CANCEL", null)
            .create()
            .apply {

                setOnShowListener {

                    getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener {

                            val durationText =
                                input.text.toString().trim()

                            if (durationText.isEmpty()) {
                                Toast.makeText(
                                    this@MyRentalsActivity,
                                    "Duration cannot be empty",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@setOnClickListener
                            }

                            val newDuration =
                                durationText.toIntOrNull()

                            if (newDuration == null) {
                                Toast.makeText(
                                    this@MyRentalsActivity,
                                    "Duration must be a number",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@setOnClickListener
                            }

                            if (newDuration <= 0) {
                                Toast.makeText(
                                    this@MyRentalsActivity,
                                    "Duration must be more than 0",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@setOnClickListener
                            }

                            // Update rental duration in SQLite.
                            val databaseHelper = DatabaseHelper(this@MyRentalsActivity)
                            val rentalDAO = RentalDAO(databaseHelper)

                            val updateRows = rentalDAO.updateRentalDuration(rental.rentalId, newDuration)

                            if (updateRows > 0) {
                                Toast.makeText(
                                    this@MyRentalsActivity,
                                    "Update duration successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                loadMyRentals()

                                dismiss()
                            } else {
                                Toast.makeText(
                                    this@MyRentalsActivity,
                                    "Failed to update rental duration",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                }

                show()
            }
    }

    // function yang berfungsi untuk meminta konfirmasi dari user ketika ingin melakukan cancel rental
    private fun showCancelDialog(rental: MyRentalVehicles) {

        AlertDialog.Builder(this)
            .setTitle("Cancel Rental")
            .setMessage(
                "Are you sure you want to cancel ${rental.vehicleName}?"
            )
            .setPositiveButton("YES") { _, _ ->

                // Delete selected rental from SQLite.
                val databaseHelper = DatabaseHelper(this@MyRentalsActivity)
                val rentalDAO = RentalDAO(databaseHelper)

                val updatedRows = rentalDAO.updateRentalStatus(
                    rental.rentalId,
                    "CANCELLED"
                )

                if (updatedRows > 0) {
                    Toast.makeText(
                        this@MyRentalsActivity,
                        "Rental cancelled successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadMyRentals()
                } else {
                    Toast.makeText(
                        this@MyRentalsActivity,
                        "Failed to cancel rental",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            .setNegativeButton("NO", null)
            .show()
    }
}
