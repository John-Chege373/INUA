package com.example.inua.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inua.MainActivity
import com.example.inua.R
import com.example.inua.data.Organization
import com.example.inua.databinding.FragmentViewDonationBinding
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class ViewDonation : Fragment() {

    private var _binding: FragmentViewDonationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewDonationBinding.inflate(inflater, container, false)
        binding.progressBar.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val organization = arguments?.getParcelable<Organization>("organization")

        organization?.let {
            binding.donationName.text = it.name
            binding.longDescription.text = it.longDescription
            Picasso.get().load(it.image).into(binding.imageView)
            binding.progressBar.visibility = View.GONE
        }

        (activity as? MainActivity)?.setBottomNavigationVisibility(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // To avoid memory leaks
        (activity as? MainActivity)?.setBottomNavigationVisibility(true)
    }
}
