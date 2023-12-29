package com.greenusys.vehicledealerapp.contracts

import com.greenusys.vehicledealerapp.models.VehicleDetailsResponse

interface VehicleDetailsContract {

    interface Model {
        interface OnFinishedListener {
            fun onFinished(vehicleDetailsResponse: VehicleDetailsResponse?)

            fun onFailure(t: Throwable?)
        }

        fun getVehicleDetailsResponse(
            onFinishedListener: OnFinishedListener?, strToken: String?, strVehicleId: String?
        )
    }

    interface View {
        fun showProgress()
        fun hideProgress()
        fun getVehicleDetailsData(vehicleDetailsResponse: VehicleDetailsResponse?)
        fun onResponseFailure(throwable: Throwable?)
    }

    interface Presenter {
        fun onDestroy()
        fun requestVehicleListDataFromServer(strToken: String?, strVehicleId: String?)
    }
}