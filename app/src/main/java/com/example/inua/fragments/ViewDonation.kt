package com.example.inua.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.inua.R
import com.example.inua.data.Organization
import com.example.inua.databinding.FragmentHomeBinding
import com.example.inua.databinding.FragmentViewDonationBinding
import com.squareup.picasso.Picasso

class ViewDonation : Fragment() {

    private var _binding: FragmentViewDonationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewDonationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val organization = arguments?.getParcelable<Organization>("organization")

        organization?.let {
            view.donationName.text = it.name // Assuming donationName is a TextView for the name
            view.longDescription.text = it.longDescription // Assuming longDescription is a TextView for the description
            Picasso.get().load(it.image).into(view.imageView)
        }
    }


}