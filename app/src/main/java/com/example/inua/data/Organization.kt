package com.example.inua.data

data class Organization(
    val contactInfo: ContactInfo,
    val id: Int,
    val image: String,
    val longDescription: String,
    val name: String,
    val shortDescription: String,
    val totalDonations: Int
)