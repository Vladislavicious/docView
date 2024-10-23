package com.example.pr2v6

import Consultation
import ConsultationList
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShopConsultationAdapter (private val mConsultations: List<Consultation>,
                           private val mContext: Context?) : RecyclerView.Adapter<ShopConsultationAdapter.ViewHolder>()
{
    private var onClickListener: OnClickListener? = null
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val timestampView = itemView.findViewById<TextView>(R.id.timestampHeader)
        val doctorNameView = itemView.findViewById<TextView>(R.id.consultationDoctorNameHeader)
        val buttonView = itemView.findViewById<Button>(R.id.signUpButton)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val consultationView = inflater.inflate(R.layout.item_shop_consultation, parent, false)
        // Return a new holder instance
        return ViewHolder(consultationView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get the data model based on position
        val consultation: Consultation = mConsultations.get(position)
        // Set item views based on your views and data model
        val timestampView = viewHolder.timestampView
        timestampView.setText(consultation.getTimeFormatted())

        val doctorName = viewHolder.doctorNameView
        doctorName.setText(consultation.doctorName)

        val button = viewHolder.buttonView

        decideButtonAppearance(button, true )

        button.setOnClickListener {
            decideButtonAppearance(button, true)

            ConsultationList.remove(consultation)
//            notifyItemRemoved(position)
            if( position == 0)
                notifyDataSetChanged()
            else
                notifyItemRemoved(position)
        }

        viewHolder.itemView.setOnClickListener {
            onClickListener?.onClick(position, consultation)
        }
    }

    private fun isConsultationChecked( consultation: Consultation ): Boolean {
        return ConsultationList.contains(consultation)
    }


    private fun decideButtonAppearance( button : Button, isChecked: Boolean )
    {
        val signString = mContext!!.resources.getString(R.string.SignUpButtonString)
        val unsignString = mContext.resources.getString(R.string.AntiSignUpButtonString)
        val antiSignUpColor = mContext.resources.getColor(R.color.dangerous)
        val signUpColor = mContext.resources.getColor(R.color.purple_500)

        if( isChecked )
        {
            button.text = unsignString
            button.background.setTint(antiSignUpColor)
        }
        else
        {
            button.text = signString
            button.background.setTint(signUpColor)
        }
    }

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    // Interface for the click listener
    interface OnClickListener {
        fun onClick(position: Int, model: Consultation)
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mConsultations.size
    }


}