package com.example.pr2v6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import kotlin.random.Random

private const val ARG_imageName = "imageAddress"

/**
 * A simple [Fragment] subclass.
 * Use the [AvatarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AvatarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var imageName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageName = it.getString(ARG_imageName)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_avatar, container, false)

        var imageViw: ShapeableImageView = view.findViewById(R.id.imageView_inFragment1)

        imageName.let {
            Picasso.get().load(it).into(imageViw)
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param imageName Parameter 2.
         * @return A new instance of fragment FragmentOne.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(imageName: String) =
            AvatarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_imageName, imageName)
                }
            }
    }
}