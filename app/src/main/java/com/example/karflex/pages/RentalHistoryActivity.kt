package com.example.karflex.pages

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karflex.R
import com.example.karflex.adapter.RentalHistoryAdapter
import com.example.karflex.helper.BaseActivity
import com.example.karflex.database.dao.RentalDAO
import com.example.karflex.database.dbhelper.DatabaseHelper


// class buat nampilin recycler view biasa (history rental)
class RentalHistoryActivity : BaseActivity() {
    private lateinit var rentalDAO: RentalDAO
    private lateinit var rentalHistoryAdapter: RentalHistoryAdapter

    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rental_history)

        setupSharedComponents()

        val sharedPref = getSharedPreferences(
            "UserSession",
            Context.MODE_PRIVATE
        )

        userId = sharedPref.getLong("LOGGED_IN_USER_ID", -1L)

        if (userId == -1L) {
            Toast.makeText(
                this,
                "Please login first",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        val dbHelper = DatabaseHelper(this)
        rentalDAO = RentalDAO(dbHelper)

        val rvRentalHistory = findViewById<RecyclerView>(
            R.id.rvRentalHistory
        )

        val etSearch = findViewById<EditText>(
            R.id.etSearch
        )

        rentalHistoryAdapter = RentalHistoryAdapter()

        rvRentalHistory.layoutManager = LinearLayoutManager(this)

        rvRentalHistory.adapter = rentalHistoryAdapter

        loadRentalHistory()

        etSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                loadRentalHistory(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadRentalHistory(searchQuery: String = "") {
        val rentalHistoryList = rentalDAO.getRentalHistory(
            userId,
            searchQuery
        )

        rentalHistoryAdapter.updateData(rentalHistoryList)
    }
}