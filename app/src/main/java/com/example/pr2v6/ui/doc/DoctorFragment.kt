package com.example.pr2v6.ui.doc

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.pr2v6.R

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

        val text = view.findViewById<TextView>(R.id.doctorTextId)

        //TODO: брать DoctorList из APP, чтобы везде точно была одна и та же инстанция

        text.text = DoctorList.getLastListItem(doctorIndex).fullName
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