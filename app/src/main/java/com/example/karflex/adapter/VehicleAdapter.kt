package com.example.karflex.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.karflex.R
import com.example.karflex.model.Vehicle
import com.example.karflex.pages.VerifyActivity

class VehicleAdapter(private val vehicleList: List<Vehicle>) :
    RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivVehicle: ImageView = itemView.findViewById(R.id.ivVehicle)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvVehicleName: TextView = itemView.findViewById(R.id.tvVehicleName)
        val tvVehicleType: TextView = itemView.findViewById(R.id.tvVehicleType)
        val tvVehiclePrice: TextView = itemView.findViewById(R.id.tvVehiclePrice)
        val btnRent: Button = itemView.findViewById(R.id.btnRent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehicle, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicleList[position]
        holder.ivVehicle.setImageResource(vehicle.imageResId)
        holder.tvVehicleName.text = vehicle.name
        holder.tvVehicleType.text = vehicle.type
        holder.tvVehiclePrice.text = vehicle.priceDisplay

        holder.btnRent.setOnClickListener {
        val context = holder.itemView.context
        val intent = Intent(context, VerifyActivity::class.java).apply {
            putExtra("VEHICLE_NAME", vehicle.name)
            putExtra("VEHICLE_PRICE", vehicle.pricePerDay)
            putExtra("VEHICLE_IMAGE", vehicle.imageResId)
        }
        context.startActivity(intent)
    }
    }

    override fun getItemCount(): Int = vehicleList.size
}