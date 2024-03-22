package com.example.inua.data

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Organization(
    val contactInfo: ContactInfo = ContactInfo(), // Assuming ContactInfo also has a no-argument constructor
    val id: Int = 0,
    val image: String = "",
    val longDescription: String = "",
    val name: String = "",
    val shortDescription: String = "",
    val totalDonations: Int = 0
) : Parcelable
