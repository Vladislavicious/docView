package com.example.pr2v6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.DoctorReview

class ReviewAdapter (private val mDoctorReviews: List<DoctorReview>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val HeaderView = itemView.findViewById<TextView>(R.id.reviewHeader)
        val RatingView = itemView.findViewById<TextView>(R.id.reviewRating)
        val ReviewView = itemView.findViewById<TextView>(R.id.reviewText)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_review, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get the data model based on position
        val doctorReview: DoctorReview = mDoctorReviews.get(position)
        // Set item views based on your views and data model

        val Header = viewHolder.HeaderView
        Header.setText(doctorReview.getViewString())

        val Rating = viewHolder.RatingView
        Rating.setText(doctorReview.rating.toString())

        val Review = viewHolder.ReviewView
        Review.setText(doctorReview.reviewText)
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mDoctorReviews.size
    }


}