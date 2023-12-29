package com.greenusys.vehicledealerapp.models

import com.google.gson.annotations.SerializedName

data class UserRegisterResponse(
    @SerializedName("token") var token: String? = null,
    @SerializedName("error") var error: String? = null
)
