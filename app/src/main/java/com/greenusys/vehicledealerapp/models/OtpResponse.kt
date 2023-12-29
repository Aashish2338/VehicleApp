package com.greenusys.vehicledealerapp.models

import com.google.gson.annotations.SerializedName

data class OtpResponse(
    @SerializedName("newuser") var newuser: OtpResponseData? = OtpResponseData(),
) {
    data class OtpResponseData(
        @SerializedName("_id") var Id: String? = null,
        @SerializedName("email") var email: String? = null,
        @SerializedName("isVerified") var isVerified: Boolean? = null,
        @SerializedName("verificationCode") var verificationCode: Int? = null,
        @SerializedName("otpExpires") var otpExpires: String? = null,
        @SerializedName("vehicles") var vehicles: ArrayList<String> = arrayListOf(),
        @SerializedName("createdAt") var createdAt: String? = null,
        @SerializedName("__v") var _v: Int? = null,
        @SerializedName("city") var city: String? = null,
        @SerializedName("mobile") var mobile: String? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("token") var token: String? = null,
        @SerializedName("refreshToken") var refreshToken: String? = null
    )
}
