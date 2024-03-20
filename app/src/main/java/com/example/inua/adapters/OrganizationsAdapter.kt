package com.example.inua.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inua.data.Organization
import com.example.inua.databinding.DonationsItemBinding
class OrganizationsAdapter
    (private val organizations: List<Organization>) :
    RecyclerView.Adapter<OrganizationsAdapter.OrganizationViewHolder>() {
    class OrganizationViewHolder(val binding: DonationsItemBinding) : RecyclerView.ViewHolder(binding.root) {


    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrganizationViewHolder {
        return OrganizationViewHolder(DonationsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val currentItem = organizations[position]
        holder.apply {
            binding.apply {
                donationName.text = currentItem.name
                shortDescription.text = currentItem.shortDescription
            }
        }
    }

    override fun getItemCount() = organizations.size

}