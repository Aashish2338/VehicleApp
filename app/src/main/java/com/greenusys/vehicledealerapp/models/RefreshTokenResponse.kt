package com.greenusys.vehicledealerapp.models

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(@SerializedName("accessToken") var accessToken: String? = null)
