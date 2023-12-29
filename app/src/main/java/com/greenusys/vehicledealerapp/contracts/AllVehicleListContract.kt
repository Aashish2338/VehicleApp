package com.greenusys.vehicledealerapp.contracts

import com.greenusys.vehicledealerapp.models.VehicleListResponse

interface AllVehicleListContract {

    interface Model {
        interface OnFinishedListener {
            fun onFinished(vehicleListResponse: VehicleListResponse?)
            fun tokenExpired(code: Int)
            fun onFailure(t: Throwable?)
        }

        fun getAllVehicleListResponse(
            onFinishedListener: OnFinishedListener?, strToken: String?
        )
    }

    interface View {
        fun showProgress()
        fun hideProgress()
        fun getAllVehicleListData(vehicleListResponse: VehicleListResponse?)
        fun onResponseFailure(throwable: Throwable?)
        fun tokenExpiredRefresh(code: Int)
    }

    interface Presenter {
        fun onDestroy()
        fun requestAllVehicleListFromServer(strToken: String?)
        fun newRefreshToken(code: Int)
    }
}