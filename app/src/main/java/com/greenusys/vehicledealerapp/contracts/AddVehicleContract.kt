package com.greenusys.vehicledealerapp.contracts

import com.greenusys.vehicledealerapp.models.VehicleResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AddVehicleContract {

    interface Model {
        interface OnFinishedListener {
            fun onFinished(vehicleResponse: VehicleResponse?)

            fun onFailure(t: Throwable?)
        }

        fun getAddVehicleDetailsResponse(
            onFinishedListener: OnFinishedListener?,
            strToken: String?,
            strVehicleNumber: RequestBody?,
            strHypotheticalRC: RequestBody?,
            strInsurance: RequestBody?,
            strOtherDocument: RequestBody?,
            strRcImage: MultipartBody.Part?,
            strBankNOCImage: MultipartBody.Part?,
            strInsuranceImage: MultipartBody.Part?,
            strOtherDocumentImage: MultipartBody.Part?
        )
    }

    interface View {
        fun showProgress()
        fun hideProgress()
        fun getAddVehicleDetailsData(vehicleResponse: VehicleResponse?)
        fun onResponseFailure(throwable: Throwable?)
    }

    interface Presenter {
        fun onDestroy()
        fun requestAddVehicleDetailsDataFromServer(
            strToken: String?,
            strVehicleNumber: RequestBody?,
            strHypotheticalRC: RequestBody?,
            strInsurance: RequestBody?,
            strOtherDocument: RequestBody?,
            strRcImage: MultipartBody.Part?,
            strBankNOCImage: MultipartBody.Part?,
            strInsuranceImage: MultipartBody.Part?,
            strOtherDocumentImage: MultipartBody.Part?
        )
    }
}