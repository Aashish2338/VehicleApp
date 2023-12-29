package com.greenusys.vehicledealerapp.models

import com.google.gson.annotations.SerializedName

data class SigningResponse(
//    @SerializedName("success") var success: String? = null,
//    @SerializedName("newUser") var newUser: NewUserData? = null,
//    @SerializedName("id") var id: String? = null,
//    @SerializedName("otp") var otp: Int? = null,

    @SerializedName("success") var success: String? = null,
    @SerializedName("id") var id: String? = null,
//    @SerializedName("otp") var otp: Int? = null

) {
    data class NewUserData(
        @SerializedName("email") var email: String? = null,
        @SerializedName("isVerified") var isVerified: Boolean? = null,
        @SerializedName("verificationCode") var verificationCode: Int? = null,
        @SerializedName("otpExpires") var otpExpires: String? = null,
        @SerializedName("vehicles") var vehicles: ArrayList<String> = arrayListOf(),
        @SerializedName("createdAt") var createdAt: String? = null,
        @SerializedName("_id") var Id: String? = null,
        @SerializedName("__v") var _v: Int? = null
    )
}
