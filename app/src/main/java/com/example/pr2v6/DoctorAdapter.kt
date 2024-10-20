package com.example.pr2v6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pr2v6.back.Doctor

class DoctorAdapter (private val mDoctors: List<Doctor>) : RecyclerView.Adapter<DoctorAdapter.ViewHolder>()
{
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val nameTextView = itemView.findViewById<TextView>(R.id.doctorName)
        val specTextView = itemView.findViewById<TextView>(R.id.doctorSpecialization)
        val priceTextView = itemView.findViewById<TextView>(R.id.doctorPrice)
        val ratingTextView = itemView.findViewById<TextView>(R.id.doctorRating)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_doctor, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get the data model based on position
        val doctor: Doctor = mDoctors.get(position)
        // Set item views based on your views and data model
        val nameView = viewHolder.nameTextView
        nameView.setText(doctor.fullName)

        val specView = viewHolder.specTextView
        specView.setText(doctor.specialization)

        val priceView = viewHolder.priceTextView
        priceView.setText(doctor.consultationPrice.toString())

        val ratingView = viewHolder.ratingTextView
        ratingView.setText(doctor.getDoctorsRating().toString())

    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mDoctors.size
    }
}