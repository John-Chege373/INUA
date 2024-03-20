package com.example.inua.data

data class Organization(
    val contactInfo: ContactInfo = ContactInfo(), // Assuming ContactInfo also has a no-argument constructor
    val id: Int = 0,
    val image: String = "",
    val longDescription: String = "",
    val name: String = "",
    val shortDescription: String = "",
    val totalDonations: Int = 0
)
