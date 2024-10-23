package com.example.pr2v6.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pr2v6.AvatarFragment
import com.example.pr2v6.R
import com.example.pr2v6.com.example.pr2v6.LoginActivity
import com.example.pr2v6.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ProfileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val userName: TextView = binding.userName
        ProfileViewModel.text.observe(viewLifecycleOwner) {
            userName.text = it
        }

        val userCity: TextView = binding.userCity
        ProfileViewModel.city.observe(viewLifecycleOwner) {
            userCity.text = it
        }

        val userEmail: TextView = binding.userEmail
        ProfileViewModel.email.observe(viewLifecycleOwner) {
            userEmail.text = it
        }

        ProfileViewModel.photoAddr.observe(viewLifecycleOwner) {
            val fragment = AvatarFragment.newInstance(it)

            val ft = childFragmentManager.beginTransaction()
            ft.replace(R.id.fragment1Frame, fragment, "avatarInsideOfProfile")
            ft.commit()
        }

        val exitButton: Button = binding.button
        exitButton.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            val b = Bundle()
            b.putInt("deauthorize", 1)
            intent.putExtras(b)
            startActivity(intent)
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}