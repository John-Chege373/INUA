package com.example.inua.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.callback.DarajaResult
import com.androidstudy.daraja.util.Environment
import com.example.inua.MainActivity
import com.example.inua.R
import com.example.inua.data.Organization
import com.example.inua.databinding.DialogMakeDonationBinding
import com.example.inua.databinding.FragmentViewDonationBinding
import com.example.inua.mpesa.AppUtils
import com.example.inua.mpesa.Config
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class ViewDonation : Fragment() {

    private var _binding: FragmentViewDonationBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: ProgressDialogFragment
    private lateinit var daraja: Daraja

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

        daraja = getDaraja()
        accessToken()
    }

    private fun getDaraja(): Daraja {
        return Daraja.builder(Config.CONSUMER_KEY, Config.CONSUMER_SECRET)
            .setBusinessShortCode(Config.BUSINESS_SHORTCODE)
            .setPassKey(AppUtils.passKey)
            .setTransactionType(Config.ACCOUNT_TYPE)
            .setCallbackUrl(Config.CALLBACK_URL)
            .setEnvironment(Environment.SANDBOX)
            .build()
    }


    private fun showDonationDialog(organization: Organization) {
        val dialogBinding = DialogMakeDonationBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Make a Donation to ${organization.name}")
            .setView(dialogBinding.root)

        val alertDialog = builder.create()

        dialogBinding.confirmButton.setOnClickListener {
            val amountString = dialogBinding.amountEditText.text.toString()
            val phoneNumber = dialogBinding.detailsEditText.text.toString()

            if (phoneNumber.isBlank() || amountString.isBlank()) {
                toast("Yu have left some fields blank")
                return@setOnClickListener
            }
            val amount = amountString.toInt()
            initiatePayment(phoneNumber, amount)
            alertDialog.dismiss()
            // Example: navigateToDonationFragment(amount, paymentDetails)
        }

        alertDialog.show()
    }

    private fun initiatePayment(phoneNumber: String, amount: Int) {
        val token = AppUtils.getAccessToken(requireContext())
        if (token == null) {
            accessToken()
            toast("Your access token was refreshed. Retry again.")
        } else {
            // initiate payment
            showProgressDialog()
            daraja.initiatePayment(
                token,
                phoneNumber,
                amount.toString(),
                AppUtils.generateUUID(),
                "Payment"
            ) { darajaResult ->
                dismissProgressDialog()
                when (darajaResult) {
                    is DarajaResult.Success -> {
                        val result = darajaResult.value
                        toast(result.ResponseDescription)
                    }
                    is DarajaResult.Failure -> {
                        val exception = darajaResult.darajaException
                        if (darajaResult.isNetworkError) {
                            toast(exception?.message ?: "Network error!")
                        } else {
                            toast(exception?.message ?: "Payment failed!")
                        }
                    }
                }
            }
        }
    }

    private fun accessToken() {
        // get access token
        showProgressDialog()
        daraja.getAccessToken { darajaResult ->
            dismissProgressDialog()
            when (darajaResult) {
                is DarajaResult.Success -> {
                    val accessToken = darajaResult.value
                    AppUtils.saveAccessToken(requireContext(), accessToken.access_token)
                }
                is DarajaResult.Failure -> {
                    val darajaException = darajaResult.darajaException
                    toast(darajaException?.message ?: "An error occurred!")
                }
            }
        }
    }

    private fun toast(text: String) = Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    private fun showProgressDialog(title: String = "This will only take a sec", message: String = "Loading") {
        progressDialog = ProgressDialogFragment.newInstance(title, message)
        progressDialog.isCancelable = false
        progressDialog.show(childFragmentManager, "progress")
    }

    private fun dismissProgressDialog() {
        progressDialog.dismiss()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // To avoid memory leaks
        (activity as? MainActivity)?.setBottomNavigationVisibility(true)
    }
}
