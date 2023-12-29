package com.greenusys.vehicledealerapp.models

import com.google.gson.annotations.SerializedName

data class VehicleListResponse(
    @SerializedName("vehicles") var vehicles: List<VehiclesData>
)

data class VehiclesData(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("vehicleOwner") var vehicleOwner: String? = null,
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
)
