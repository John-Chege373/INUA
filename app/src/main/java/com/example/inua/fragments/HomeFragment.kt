package com.example.inua.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.inua.R
import com.example.inua.adapters.OrganizationsAdapter
import com.example.inua.data.Organization
import com.example.inua.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
            val username = it.displayName ?: "User"
            binding.userName.text = getString(R.string.welcome_message, username)

        }

        val organizationsAdapter = OrganizationsAdapter(emptyList()) // Initially empty list
        binding.donationsList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = organizationsAdapter
        }
        //SHOW DONATIONS
        fetchOrganizationsFromFirebase()

    }

    private fun fetchOrganizationsFromFirebase() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("organizations")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val organizationsList = mutableListOf<Organization>()
                for (snapshot in dataSnapshot.children) {
                    val organization = snapshot.getValue(Organization::class.java)
                    organization?.let {
                        organizationsList.add(it)
                    }
                }
                updateOrganizationsList(organizationsList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("HomeFragment", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    private fun updateOrganizationsList(organizations: List<Organization>) {
        if (activity != null) {
            val adapter = binding.donationsList.adapter as? OrganizationsAdapter
            adapter?.updateOrganizations(organizations)
        }
    }

}