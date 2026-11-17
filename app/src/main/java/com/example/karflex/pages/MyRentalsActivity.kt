
package com.example.karflex.pages

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karflex.R
import com.example.karflex.adapter.MyRentalAdapter
import com.example.karflex.adapter.adapterModel.MyRentalVehicles
import com.example.karflex.helper.BaseActivity

class MyRentalsActivity : BaseActivity() {

    private lateinit var rvMyRentals: RecyclerView
    private lateinit var tvEmptyRental: TextView
    private lateinit var rentalAdapter: MyRentalAdapter

    private val mtRentalList = mutableListOf<MyRentalVehicles>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_rentals)

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

    private fun loadMyRentals() {

        // TODO:
        // Ambil active rental berdasarkan logged-in user dari SQLite.
        // Masukkan hasil query ke mtRentalList.

        updateRentalList()
    }

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

    private fun showUpdateDialog(rental: MyRentalVehicles) {

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.hint = "Enter new duration"
        input.setText(rental.durationRent.toString())

        AlertDialog.Builder(this)
            .setTitle("Update Rental Duration")
            .setMessage("Enter your new rental duration in days.")
            .setView(input)
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

                            // TODO:
                            // Update rental duration in SQLite.

                            Toast.makeText(
                                this@MyRentalsActivity,
                                "Duration validation successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            dismiss()
                        }
                }

                show()
            }
    }

    private fun showCancelDialog(rental: MyRentalVehicles) {

        AlertDialog.Builder(this)
            .setTitle("Cancel Rental")
            .setMessage(
                "Are you sure you want to cancel ${rental.vehicleName}?"
            )
            .setPositiveButton("YES") { _, _ ->

                // TODO:
                // Delete selected rental from SQLite.

            }
            .setNegativeButton("NO", null)
            .show()
    }
}
