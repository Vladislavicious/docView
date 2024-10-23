package com.example.pr2v6.ui.shoppingCart

import Consultation
import ConsultationList
import DoctorList
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pr2v6.DoctorActivity
import com.example.pr2v6.ShopConsultationAdapter
import com.example.pr2v6.back.Doctor
import com.example.pr2v6.databinding.FragmentShoppingCartBinding

class shoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private lateinit var noItemString: String

    private var consultations: MutableList<Consultation>? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ShoppingCartViewModel =
            ViewModelProvider(this).get(shoppingCartViewModel::class.java)

        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        val root: View = binding.root


        ShoppingCartViewModel.buttonName.observe(viewLifecycleOwner) {
            noItemString = it
        }


        val button: Button = binding.signUpButtonInShop
        val declineButton: Button = binding.declineButton

        val header: TextView = binding.shopHeader
        val recyclerConsultations = binding.consultationRecyclerInShop

        ShoppingCartViewModel.ConsultationsCount.observe(viewLifecycleOwner) {
            if( it == 0 )
            {
                hideAll()
            }
        }

        if( ConsultationList.isEmpty() )
        {
            hideAll()
            return root
        }

        val consAdapter = ShopConsultationAdapter(ConsultationList, this.context)
        //        // Attach the adapter to the recyclerview to populate items
        recyclerConsultations.adapter = consAdapter
        //        // Set layout manager to position the items
        var layoutMan = LinearLayoutManager(this.context)
        recyclerConsultations.layoutManager = layoutMan
        header.visibility = View.GONE
        button.visibility = View.VISIBLE

        consAdapter.setOnClickListener(object : ShopConsultationAdapter.OnClickListener {
            override fun onClick(position: Int, model: Consultation) {
                val intent: Intent = Intent(
                    activity,
                    DoctorActivity::class.java
                )

                val consultation = ConsultationList[position]
                val relatedDoctorIndex = DoctorList.getDoctorIndexByConsultation(consultation)

                val b = Bundle()
                b.putInt("key", relatedDoctorIndex) //Your id

                intent.putExtras(b) //Put your id to your next Intent
                startActivity(intent)
            }
        })

        button.setOnClickListener {

            Toast.makeText(this.context, "С вас ${ConsultationList.calculateTotalCost()}", Toast.LENGTH_SHORT).show()

            DoctorList.deleteConsultationsFromDoctors(ConsultationList)

            ConsultationList.payForAll()


            recyclerConsultations.invalidate()
            hideAll()
        }

        declineButton.setOnClickListener {
            Toast.makeText(this.context, "Произошла отмена", Toast.LENGTH_SHORT).show()
            ConsultationList.cancelAll()

            binding.consultationRecyclerInShop.invalidate()
            hideAll()
        }

        return root
    }

    private fun hideAll() {
        val ShoppingCartViewModel =
            ViewModelProvider(this).get(shoppingCartViewModel::class.java)

        val button: Button = binding.signUpButtonInShop
        val declineButton: Button = binding.declineButton

        val header: TextView = binding.shopHeader

        ShoppingCartViewModel.noItems.observe(viewLifecycleOwner) {
            header.text = it
        }
        header.visibility = View.VISIBLE
        button.visibility = View.GONE
        declineButton.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}