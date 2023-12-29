package com.greenusys.vehicledealerapp.contracts

import com.greenusys.vehicledealerapp.models.ResendOtpResponse

interface DeleteVehicleContract {
    interface Model {
        interface OnFinishedListener {
            fun onFinished(resendOtpResponse: ResendOtpResponse?)

            fun onFailure(t: Throwable?)
        }

        fun getDeleteVehicleResponse(
            onFinishedListener: OnFinishedListener?, strToken: String?, strId: String?
        )
    }

    interface View {
        fun showProgress()
        fun hideProgress()
        fun getDeleteVehicleData(resendOtpResponse: ResendOtpResponse?)
        fun onResponseFailure(throwable: Throwable?)
    }

    interface Presenter {
        fun onDestroy()
        fun requestDeleteVehicleDataFromServer(strToken: String?, strId: String?)
    }
}