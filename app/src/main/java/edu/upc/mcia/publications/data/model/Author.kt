package edu.upc.mcia.publications.data.model

import com.google.gson.annotations.SerializedName

data class Author(
        val id: String,
        @SerializedName("fullname") val name: String,
        val email: String,
        val photo: String,
        val status: String
)
