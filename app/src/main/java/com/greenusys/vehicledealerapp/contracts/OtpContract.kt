package com.greenusys.vehicledealerapp.contracts

import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.models.OtpResponse

interface OtpContract {

    interface Model {
        interface OnFinishedListener {
            fun onFinished(otpResponse: OtpResponse?)

            fun onFailure(t: Throwable?)
        }

        fun getOtpResponse(
            onFinishedListener: OnFinishedListener?, jsonObject: JsonObject?
        )
    }

    interface View {
        fun showProgress()
        fun hideProgress()
        fun getLoginOtpData(otpResponse: OtpResponse?)
        fun onResponseFailure(throwable: Throwable?)
    }

    interface Presenter {
        fun onDestroy()
        fun requestOtpDataFromServer(jsonObject: JsonObject?)
    }
}