package com.example.pr2v6.ui.home

import DoctorList
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pr2v6.DoctorActivity
import com.example.pr2v6.DoctorAdapter
import com.example.pr2v6.R
import com.example.pr2v6.back.Doctor
import com.example.pr2v6.back.getRandomDoctor
import com.example.pr2v6.databinding.FragmentHomeBinding


internal val DOCTORS_LIST_FILENAME = "doctors.json"

val PRICE_OPTION_STR: String = "Цена"
val SPEC_OPTION_STR: String = "Специализация"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var textView: TextView
    private lateinit var editText: EditText

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


        if( false == DoctorList.initialize(DOCTORS_LIST_FILENAME) )
        {
            Log.i("home","bad doctor list init")
            for( i in 0..20 )
                DoctorList.add(getRandomDoctor())
        }
        /// search

        textView = root.findViewById(R.id.textView)
        editText = root.findViewById(R.id.editText)

        // Настройка текстового поля для открытия подменю
        textView.setOnClickListener {
            showPopupMenu(it)
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Реакция на ввод текста
                handleInputChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        initializeAdapter(DoctorList)

        return root
    }

    private fun initializeAdapter( doctorsList: List<Doctor> )
    {

        val recyclerDoctors: RecyclerView = binding.root.findViewById(R.id.recyclerDoctors)
//        // Create adapter passing in the sample user data
        val adapter = DoctorAdapter(doctorsList)
//        // Attach the adapter to the recyclerview to populate items
        recyclerDoctors.adapter = adapter
//        // Set layout manager to position the items
        var layoutMan = LinearLayoutManager(this.context)
        recyclerDoctors.layoutManager = layoutMan

        adapter.setOnClickListener(object : DoctorAdapter.OnClickListener {
            override fun onClick(position: Int, model: Doctor) {
                val intent: Intent = Intent(
                    activity,
                    DoctorActivity::class.java
                )

                val b = Bundle()
                b.putInt("key", position) //Your id
                intent.putExtras(b) //Put your id to your next Intent
                startActivity(intent)
            }
        })
        recyclerDoctors.invalidate()

    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu( this.activity, view)
        popupMenu.menu.add(PRICE_OPTION_STR)
        popupMenu.menu.add(SPEC_OPTION_STR)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            textView.text = menuItem.title // Обновляем текстовое поле

            if( menuItem.title == SPEC_OPTION_STR )
                editText.hint = "Введите специализацию"
            else
                editText.hint = "Введите цену"

            true
        }

        popupMenu.show()
    }

    private fun handleInputChange(input: String) {
        // Реакция на изменение текста в поле ввода

        if( !input.contains("\n") )
            return

        editText.setText("")

        if( input.length == 1 ) // Просто введён Enter
        {
            initializeAdapter(DoctorList.findDoctorsWithPriceLessThan(Float.MAX_VALUE))
            return
        }

        if( input.length >= 21 ) {
            Toast.makeText(activity, "line too long", Toast.LENGTH_SHORT).show()
            return
        }

        val cleanInput = input.trim()

        if( textView.text == PRICE_OPTION_STR )
        {
            val number = cleanInput.toFloatOrNull()
            if( number == null ) {
                Toast.makeText(activity, "not a number", Toast.LENGTH_SHORT).show()
                return
            }

            initializeAdapter(DoctorList.findDoctorsWithPriceLessThan(number))
            return
        }

        if( textView.text == SPEC_OPTION_STR )
        {
            initializeAdapter(DoctorList.findDoctorsWithSameSpecialization(cleanInput))
        }
        else {
            Toast.makeText(activity, "параметр не выбран", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}