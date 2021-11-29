package com.electchain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("token")
    @Expose
    var token: String = ""
    @SerializedName("message")
    @Expose
    var message: String = ""
}