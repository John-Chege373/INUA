package com.example.inua.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.callback.DarajaResult
import com.androidstudy.daraja.util.Environment
import com.example.inua.databinding.FragmentDonationsBinding
import com.example.inua.mpesa.AppUtils
import com.example.inua.mpesa.Config

class DonationsFragment : Fragment() {
    private var _binding: FragmentDonationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: ProgressDialogFragment
    private lateinit var daraja: Daraja

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDonationsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        daraja = getDaraja()

        accessToken()

        binding.bPay.setOnClickListener { pay() }
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

    private fun pay() {
        val phoneNumber = binding.etPhoneNumber.text.toString()
        val amountString = binding.etAmount.text.toString()

        if (phoneNumber.isBlank() || amountString.isBlank()) {
            toast("You have left some fields blank")
            return
        }

        val amount = amountString.toInt()
        initiatePayment(phoneNumber, amount)
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

}