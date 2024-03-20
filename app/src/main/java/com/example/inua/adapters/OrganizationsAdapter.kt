package com.example.inua.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inua.data.Organization
import com.example.inua.databinding.DonationsItemBinding
import com.squareup.picasso.Picasso

class OrganizationsAdapter(private var organizations: List<Organization>) :
    RecyclerView.Adapter<OrganizationsAdapter.OrganizationViewHolder>() {

    class OrganizationViewHolder(val binding: DonationsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val binding = DonationsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrganizationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val currentItem = organizations[position]
        with(holder.binding) {
            donationName.text = currentItem.name
            shortDescription.text = currentItem.shortDescription
            Picasso.get().load(currentItem.image).into(imageView)
            // If you have set up any click listeners for the view, they remain here.
        }
    }

    override fun getItemCount() = organizations.size

    // Method to update the list of organizations
    @SuppressLint("NotifyDataSetChanged")
    fun updateOrganizations(newOrganizations: List<Organization>) {
        organizations = newOrganizations
        notifyDataSetChanged() // Notifies the adapter to refresh the data
    }
}
