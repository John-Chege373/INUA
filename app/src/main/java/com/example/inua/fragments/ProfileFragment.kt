package com.example.inua.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.inua.MainActivity
import com.example.inua.R
import com.example.inua.auth.SignIn
import com.example.inua.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        account?.let {
            Picasso.get().load(it.photoUrl).into(binding.imgProfile)
            binding.userName.text = it.displayName
        }

        binding.editProfile.setOnClickListener {

        }


        binding.viewDonations.setOnClickListener {
            val donationsFragment = DonationsFragment()

            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.flFragment, donationsFragment)
                .addToBackStack(null)
                .commit()
            (activity as? MainActivity)?.setBottomNavigationVisibility(true)
        }

        binding.logout.setOnClickListener {
            GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN)
                .signOut()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(activity, SignIn::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        Toast.makeText(activity,
                            "Logging out",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(intent)
                        activity?.finish()
                    } else {
                        Toast.makeText(
                            activity,
                            "An error occurred while trying to log out",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }


    }

}