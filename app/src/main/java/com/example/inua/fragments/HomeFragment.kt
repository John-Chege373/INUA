package com.example.inua.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.inua.adapters.OrganizationsAdapter
import com.example.inua.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        account?.let {
            binding.userName.text = it.displayName

        }
        val organizationsAdapter = OrganizationsAdapter(listOf()) // Initially empty list
        binding.donationsList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = organizationsAdapter
        }
        //SHOW DONATIONS
        fetchOrganizationsFromFirebase()

    }

    private fun fetchOrganizationsFromFirebase() {

    }

}