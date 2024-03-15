package com.example.inua.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inua.R
import com.example.inua.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

        account?.let {
            val username = it.displayName ?: "User"
            binding.welcomeMessage.text = getString(R.string.welcome_message, username)

        }

        //SHOW DONATIONS

    }

}