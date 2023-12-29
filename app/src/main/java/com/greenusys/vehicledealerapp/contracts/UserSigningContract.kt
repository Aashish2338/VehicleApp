package com.greenusys.vehicledealerapp.contracts

import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.models.SigningResponse

interface UserSigningContract {

    interface Model {
        interface OnFinishedListener {
            fun onFinished(signingResponse: SigningResponse?)

            fun onFailure(t: Throwable?)
        }

        fun getSignResponse(onFinishedListener: OnFinishedListener?, jsonObject: JsonObject?)
    }

    interface View {
        fun showProgress()
        fun hideProgress()
        fun getLoginUserData(signingResponse: SigningResponse?)
        fun onResponseFailure(throwable: Throwable?)
    }

    interface Presenter {
        fun onDestroy()
        fun requestDataFromServer(jsonObject: JsonObject?)
    }
}