package com.greenusys.vehicledealerapp.models

import com.google.gson.annotations.SerializedName

data class VehicleDetailsResponse(@SerializedName("vehicle") var vehicle: VehicleDetails? = VehicleDetails()) {

    data class VehicleDetails(
        @SerializedName("_id") var Id: String? = null,
        @SerializedName("vehicleOwner") var vehicleOwner: VehicleOwner? = VehicleOwner(),
        @SerializedName("vehicleNumber") var vehicleNumber: String? = null,
        @SerializedName("chassisNumber") var chassisNumber: String? = null,
        @SerializedName("engineNumber") var engineNumber: String? = null,
        @SerializedName("hypotheticationRC") var hypotheticationRC: Boolean? = null,
        @SerializedName("BankNOCImage") var BankNOCImage: String? = null,
        @SerializedName("insuranceValid") var insuranceValid: Boolean? = null,
        @SerializedName("insuranceImage") var insuranceImage: String? = null,
        @SerializedName("otherDocument") var otherDocument: Boolean? = null,
        @SerializedName("otherDocumentImage") var otherDocumentImage: String? = null,
        @SerializedName("rcImage") var rcImage: String? = null,
        @SerializedName("createdAt") var createdAt: String? = null,
        @SerializedName("__v") var _v: Int? = null
    ) {
        data class VehicleOwner(
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
        )
    }
}
