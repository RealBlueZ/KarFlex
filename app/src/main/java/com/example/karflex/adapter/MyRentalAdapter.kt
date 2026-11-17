package com.example.karflex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.karflex.R
import com.example.karflex.adapter.adapterModel.MyRentalVehicles
import org.w3c.dom.Text

class MyRentalAdapter(
    private val mtRentalList: List<MyRentalVehicles>,
    private val onUpdateClick: (MyRentalVehicles) -> Unit,
    private val onCancelClick: (MyRentalVehicles) -> Unit
): RecyclerView.Adapter<MyRentalAdapter.MyRentalViewHolder>() {
    class MyRentalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivVehicleImage: ImageView = itemView.findViewById(R.id.ivVehicleImage)
        val tvRentalStatus: TextView = itemView.findViewById(R.id.tvRentalStatus)
        val tvVehicleName: TextView = itemView.findViewById(R.id.tvVehicleName)
        val tvStartDate: TextView = itemView.findViewById(R.id.tvStartDate)
        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        val tvEndDate: TextView = itemView.findViewById(R.id.tvEndDate)
        val btnUpdateDuration: Button = itemView.findViewById(R.id.btnUpdateDuration)
        val btnCancelRental: Button = itemView.findViewById(R.id.btnCancelRental)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyRentalAdapter.MyRentalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_my_rentals, parent, false)

        return MyRentalViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyRentalAdapter.MyRentalViewHolder,
        position: Int
    ) {
        val myRental = mtRentalList[position]

        holder.tvRentalStatus.text = myRental.statusRent
        holder.tvVehicleName.text = myRental.vehicleName
        holder.ivVehicleImage.setImageResource(myRental.vehicleImage)
        holder.tvStartDate.text = myRental.startDateRent
        holder.tvDuration.text = "${myRental.durationRent} Days"
        holder.tvEndDate.text = myRental.endDateRent

        holder.btnUpdateDuration.setOnClickListener {
            onUpdateClick(myRental)
        }

        holder.btnCancelRental.setOnClickListener {
            onCancelClick(myRental)
        }
    }

    override fun getItemCount(): Int {
        return mtRentalList.size
    }
}