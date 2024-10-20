package com.example.pr2v6.ui.home

import DoctorList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pr2v6.DoctorAdapter
import com.example.pr2v6.R
import com.example.pr2v6.back.Doctor
import com.example.pr2v6.back.getRandomDoctor
import com.example.pr2v6.databinding.FragmentHomeBinding

internal val DOCTORS_LIST_FILENAME = "doctors.json"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val rvContacts: RecyclerView = root.findViewById(R.id.rvContacts)
        // Initialize contacts

        if( false == DoctorList.initialize(DOCTORS_LIST_FILENAME) )
        {
            Log.i("home","bad doctor list init")
            for( i in 0..20 )
                DoctorList.add(getRandomDoctor())
        }

//        // Create adapter passing in the sample user data
        val adapter = DoctorAdapter(DoctorList, this)
//        // Attach the adapter to the recyclerview to populate items
        rvContacts.adapter = adapter
//        // Set layout manager to position the items
        var layoutMan = LinearLayoutManager(this.context)
        rvContacts.layoutManager = layoutMan

        adapter.setOnClickListener(object : DoctorAdapter.OnClickListener {
            override fun onClick(position: Int, model: Doctor) {
                Toast.makeText(activity, "click $position!", Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}