package com.electchain.models

data class User(
    var firstName: String = "",
    var lastName: String = "",
    var dob: String = "",
    var phone: String = "",
    var email: String = "",
    var voterId: String = "",
    var otpCode: String = "",
    var token: String = "",
)