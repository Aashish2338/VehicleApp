package com.greenusys.vehicledealerapp.models

import com.google.gson.annotations.SerializedName

data class VehicleResponse(
    @SerializedName("success") var success: String? = null,
    @SerializedName("newVehicle") var newVehicle: NewVehicle? = NewVehicle()
) {
    data class NewVehicle(
        @SerializedName("vehicleOwner") var vehicleOwner: String? = null,
        @SerializedName("vehicleNumber") var vehicleNumber: String? = null,
        @SerializedName("chassisNumber") var chassisNumber: String? = null,
        @SerializedName("engineNumber") var engineNumber: String? = null,
        @SerializedName("rcImage") var rcImage: String? = null,
        @SerializedName("numberPlateImage") var numberPlateImage: String? = null,
        @SerializedName("chassisImage") var chassisImage: String? = null,
        @SerializedName("createdAt") var createdAt: String? = null,
        @SerializedName("_id") var Id: String? = null,
        @SerializedName("__v") var _v: Int? = null
    )
}
