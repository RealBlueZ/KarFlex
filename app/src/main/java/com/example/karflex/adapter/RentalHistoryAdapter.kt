package com.example.karflex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.karflex.R
import com.example.karflex.model.RentalVehicle

class RentalHistoryAdapter(private val rentalHistoryList: List<RentalVehicle>):
    RecyclerView.Adapter<RentalHistoryAdapter.RentalHistoryViewHolder>() {
    class RentalHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val ivVehicle: ImageView = itemView.findViewById(R.id.ivVehicle)

        val tvVehicleName: TextView = itemView.findViewById(R.id.tvVehicleName)

        val tvVehicleBrand: TextView = itemView.findViewById(R.id.tvVehicleBrand)

        val tvRentalStatus: TextView = itemView.findViewById(R.id.tvRentalStatus)

        val tvStartDate: TextView = itemView.findViewById(R.id.tvStartDate)

        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RentalHistoryAdapter.RentalHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rental_history, parent, false)

        return RentalHistoryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RentalHistoryAdapter.RentalHistoryViewHolder,
        position: Int
    ) {
        val rental = rentalHistoryList[position]

        holder.ivVehicle.setImageResource(rental.vehicleImage)
        holder.tvVehicleName.text = rental.vehicleName
        holder.tvVehicleBrand.text = rental.vehicleBrand
        holder.tvDuration.text = "${rental.durationRent} Days"
        holder.tvStartDate.text = rental.startDateRent

        when (rental.statusRent.lowercase()){
            "active" -> {
                holder.tvRentalStatus.text = "ACTIVE"
                holder.tvRentalStatus.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        android.R.color.holo_green_dark
                    )
                )
                holder.tvRentalStatus.setBackgroundResource(
                    R.drawable.bg_status_active
                )
            }

            "completed" -> {
                holder.tvRentalStatus.text = "COMPLETED"
                holder.tvRentalStatus.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        android.R.color.darker_gray
                    )
                )
                holder.tvRentalStatus.setBackgroundResource(
                    R.drawable.bg_status_completed
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return rentalHistoryList.size
    }
}