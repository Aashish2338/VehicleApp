package com.greenusys.vehicledealerapp.contracts

import com.greenusys.vehicledealerapp.models.ResendOtpResponse

interface ResendOtpContract {
    interface Model {
        interface OnFinishedListener {
            fun onFinished(resendOtpResponse: ResendOtpResponse?)

            fun onFailure(t: Throwable?)
        }

        fun getResendOtpResponse(
            onFinishedListener: OnFinishedListener?, strId: String?
        )
    }

    interface View {
        fun showProgress()
        fun hideProgress()
        fun getResendOtpData(resendOtpResponse: ResendOtpResponse?)
        fun onResponseFailure(throwable: Throwable?)
    }

    interface Presenter {
        fun onDestroy()
        fun requestResendOtpDataFromServer(strId: String?)
    }
}