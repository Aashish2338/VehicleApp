package com.greenusys.vehicledealerapp.models

import com.google.gson.annotations.SerializedName

data class ProfileResponse(@SerializedName("user") var user: UserProfileData? = UserProfileData()) {
    data class UserProfileData(
        @SerializedName("_id") var Id: String? = null,
        @SerializedName("email") var email: String? = null,
        @SerializedName("vehicles") var vehicles: ArrayList<UserVehiclesData> = arrayListOf(),
        @SerializedName("__v") var _v: Int? = null,
        @SerializedName("city") var city: String? = null,
        @SerializedName("mobile") var mobile: String? = null,
        @SerializedName("name") var name: String? = null
    ) {
        data class UserVehiclesData(
            @SerializedName("_id") var Id: String? = null,
            @SerializedName("vehicleOwner") var vehicleOwner: String? = null,
            @SerializedName("vehicleNumber") var vehicleNumber: String? = null,
            @SerializedName("chassisNumber") var chassisNumber: String? = null,
            @SerializedName("engineNumber") var engineNumber: String? = null,
            @SerializedName("rcImage") var rcImage: String? = null,
            @SerializedName("numberPlateImage") var numberPlateImage: String? = null,
            @SerializedName("chassisImage") var chassisImage: String? = null,
            @SerializedName("createdAt") var createdAt: String? = null,
            @SerializedName("__v") var _v: Int? = null
        )
    }
}
