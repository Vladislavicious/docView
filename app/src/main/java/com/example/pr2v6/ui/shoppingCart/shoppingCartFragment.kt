package com.example.pr2v6.ui.shoppingCart

import ConsultationList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pr2v6.ConsultationAdapter
import com.example.pr2v6.databinding.FragmentShoppingCartBinding

class shoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null

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


        val button: Button = binding.signUpButtonInShop
        ShoppingCartViewModel.buttonName.observe(viewLifecycleOwner) {
            button.text = it
        }

        val header: TextView = binding.shopHeader
        val recyclerConsultations = binding.consultationRecyclerInShop

        if( ConsultationList.isNotEmpty() )
        {
            val consAdapter = ConsultationAdapter(ConsultationList, this.context)
            //        // Attach the adapter to the recyclerview to populate items
            recyclerConsultations.adapter = consAdapter
            //        // Set layout manager to position the items
            var layoutMan = LinearLayoutManager(this.context)
            recyclerConsultations.layoutManager = layoutMan

            ShoppingCartViewModel.header.observe(viewLifecycleOwner) {
                header.text = it
            }
            button.visibility = View.VISIBLE
        }
        else
        {
            ShoppingCartViewModel.noItems.observe(viewLifecycleOwner) {
                header.text = it
            }
            button.visibility = View.GONE
        }

//        exitButton.setOnClickListener {
//            val intent = Intent(activity, LoginActivity::class.java)
//            startActivity(intent)
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}