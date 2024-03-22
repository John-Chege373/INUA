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

class HomeFragment : Fragment(), OrganizationsAdapter.OnItemButtonClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        account?.let {
            val username = it.displayName ?: "User"
            binding.userName.text = getString(R.string.welcome_message, username)
        }
        binding.progressBar.visibility = View.VISIBLE

        // Initialize your OrganizationsAdapter here and set the itemButtonClickListener
        val organizationsAdapter = OrganizationsAdapter(emptyList()).apply {
            itemButtonClickListener = this@HomeFragment
        }

        // Set up the RecyclerView
        binding.donationsList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = organizationsAdapter
        }

        // Fetch organizations from Firebase and update the RecyclerView
        fetchOrganizationsFromFirebase()
        return binding.root

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
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("HomeFragment", "loadPost:onCancelled", databaseError.toException())

                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun updateOrganizationsList(organizations: List<Organization>) {
        if (activity != null) {
            val adapter = binding.donationsList.adapter as? OrganizationsAdapter
            adapter?.updateOrganizations(organizations)
        }
    }

    override fun onItemButtonClick(organization: Organization) {
        val detailsFragment = ViewDonation().apply {
            arguments = Bundle().apply {
                putParcelable("organization", organization)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, detailsFragment)
            .addToBackStack(null)
            .commit()
    }

}