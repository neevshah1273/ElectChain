package com.electchain.models

data class User(
    val firstName: String = "",
    val lastName: String = "",
    val dob: String = "",
    val mobileNumber: String = "",
    val emailAddress: String = "",
    val voterId: String = ""
)