package com.greenusys.vehicledealerapp.contracts

import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.models.UserRegisterResponse

interface UserRegisterContract {
    interface Model {
        interface OnFinishedListener {
            fun onFinished(userRegisterResponse: UserRegisterResponse?)

            fun onFailure(t: Throwable?)
        }

        fun getRegisterResponse(
            onFinishedListener: OnFinishedListener?, jsonObject: JsonObject?
        )
    }

    interface View {
        fun showProgress()
        fun hideProgress()
        fun getRegisterData(userRegisterResponse: UserRegisterResponse?)
        fun onResponseFailure(throwable: Throwable?)
    }

    interface Presenter {
        fun onDestroy()
        fun requestRegisterDataFromServer(jsonObject: JsonObject?)
    }
}