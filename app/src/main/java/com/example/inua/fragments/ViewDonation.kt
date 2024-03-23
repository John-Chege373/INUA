package com.example.inua.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inua.MainActivity
import com.example.inua.R
import com.example.inua.data.Organization
import com.example.inua.databinding.DialogMakeDonationBinding
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
            //CONTACT INFO
            binding.email.text = getString(R.string.email_format, it.contactInfo.email)
            binding.phone.text = getString(R.string.phone_format, it.contactInfo.phone)
            binding.address.text = getString(R.string.address_format, it.contactInfo.address)
            binding.progressBar.visibility = View.GONE
        }

        binding.makeDonation.setOnClickListener {
            organization?.let { org ->
                showDonationDialog(org)
            }
        }

        (activity as? MainActivity)?.setBottomNavigationVisibility(false)
    }


    private fun showDonationDialog(organization: Organization) {
        val dialogBinding = DialogMakeDonationBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Make a Donation to ${organization.name}")
            .setView(dialogBinding.root)

        val alertDialog = builder.create()

        dialogBinding.confirmButton.setOnClickListener {
            val amount = dialogBinding.amountEditText.text.toString()
            val paymentDetails = dialogBinding.detailsEditText.text.toString()
            // Here, you'd handle the donation logic...
            alertDialog.dismiss()
            // Example: navigateToDonationFragment(amount, paymentDetails)
        }

        alertDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // To avoid memory leaks
        (activity as? MainActivity)?.setBottomNavigationVisibility(true)
    }
}
