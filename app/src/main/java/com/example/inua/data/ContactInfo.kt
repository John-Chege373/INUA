package com.example.inua.data

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class ContactInfo(
    val address: String = "",
    val email: String = "",
    val phone: String = ""
) : Parcelable
