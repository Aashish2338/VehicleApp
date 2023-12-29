package com.greenusys.vehicledealerapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenusys.vehicledealerapp.R
import com.greenusys.vehicledealerapp.activities.VehicleDetailsActivity
import com.greenusys.vehicledealerapp.models.VehiclesData
import com.greenusys.vehicledealerapp.utilities.UserSession

class DealerListAdapter(
    private var mContext: Context, private var vehicles: List<VehiclesData>
) : RecyclerView.Adapter<DealerListAdapter.DealerListViewHolder>() {

    private var userSession: UserSession = UserSession(mContext)

    private val baseUrl: String = "http://testnode.shivshankartransport.xyz/"
//    private val baseUrl: String = "https://wheelo.onrender.com/"
//    private val baseUrl: String = "http://192.168.1.13:5000/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealerListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dealers_item_layout, parent, false)
        return DealerListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vehicles.size
    }

    override fun onBindViewHolder(holder: DealerListViewHolder, position: Int) {
        val vehicleData: VehiclesData = vehicles[position]

        if (vehicleData.vehicleNumber != null) {
            holder.rcNumber_Tv.text = vehicleData.vehicleNumber
        } else {
            holder.rcNumber_Tv.text = "N/A"
        }

        if (vehicleData.hypotheticationRC != null) {
            holder.hypothetic_Tv.text = vehicleData.hypotheticationRC.toString()
        } else if (vehicleData.chassisNumber != null) {
            holder.hypothetic_Tv.text = vehicleData.chassisNumber.toString()
        } else {
            holder.hypothetic_Tv.text = "N/A"
        }

        if (vehicleData.insuranceValid != null) {
            holder.insurance_Tv.text = vehicleData.insuranceValid.toString()
        } else if (vehicleData.vehicleNumber != null) {
            holder.insurance_Tv.text = vehicleData.vehicleNumber.toString()
        } else {
            holder.insurance_Tv.text = "N/A"
        }

        if (vehicleData.otherDocument != null) {
            holder.othersDocs_Tv.text = vehicleData.otherDocument.toString()
        } else {
            holder.othersDocs_Tv.text = "N/A"
        }

        if (vehicleData.rcImage != null) {
            val strImage: String? = vehicleData.rcImage
//            val newImage: String? = strImage?.replace("public\\images\\", "")
            val newImage: String? = strImage?.replace("public/images/", "")
            println("Replace Images :- $newImage")
            Glide.with(mContext).load(baseUrl + newImage).into(holder.rCImage)
        } else {
            holder.rCImage.setImageResource(R.drawable.no_image)
        }

        holder.itemView.setOnClickListener { _ ->
            userSession.setVehicleId(vehicleData.Id)
            userSession.setUserId(vehicleData.vehicleOwner)
            mContext.startActivity(
                Intent(
                    mContext, VehicleDetailsActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    class DealerListViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val rCImage: ImageView = itemView.findViewById<ImageView>(R.id.rCImage) as ImageView
        val rcNumber_Tv: TextView = itemView.findViewById<TextView>(R.id.rcNumber_Tv) as TextView
        val hypothetic_Tv: TextView =
            itemView.findViewById<TextView>(R.id.hypothetic_Tv) as TextView
        val insurance_Tv: TextView = itemView.findViewById<TextView>(R.id.insurance_Tv) as TextView
        val othersDocs_Tv: TextView =
            itemView.findViewById<TextView>(R.id.othersDocs_Tv) as TextView
    }
}