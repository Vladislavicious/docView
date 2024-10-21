package com.example.pr2v6.ui.doc

import IMAGES
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.pr2v6.R
import com.squareup.picasso.Picasso

internal val ARG_doctorIndex = "docIndex"

class DoctorFragment : Fragment() {

    private val viewModel: DoctorViewModel by viewModels()
    private var doctorIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doctorIndex = it.getInt(ARG_doctorIndex)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_doctor, container, false)

        val nameTextView = view.findViewById<TextView>(R.id.doctorFragmentName)
        val specTextView = view.findViewById<TextView>(R.id.doctorFragmentSpecialization)
        val priceTextView = view.findViewById<TextView>(R.id.doctorFragmentPrice)
        val ratingTextView = view.findViewById<TextView>(R.id.doctorFragmentRating)
        val imageView = view.findViewById<ImageView>(R.id.imageViewInDoctorFragment)

        val doctor = DoctorList.getLastListItem(doctorIndex)

        nameTextView.text = doctor.getFullNameFormatted()
        specTextView.text = doctor.specialization
        priceTextView.text = doctor.consultationPrice.toString()
        ratingTextView.text = doctor.getDoctorsRating().toString()

        Picasso.get().load(IMAGES[doctorIndex % IMAGES.size]).into(imageView)

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(doctorIndex: Int) =
            DoctorFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_doctorIndex, doctorIndex)
                }
            }
    }

}