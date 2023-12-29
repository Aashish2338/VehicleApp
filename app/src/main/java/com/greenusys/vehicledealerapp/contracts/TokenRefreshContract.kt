package com.greenusys.vehicledealerapp.contracts

import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.models.RefreshTokenResponse

interface TokenRefreshContract {
    interface Model {
        interface OnFinishedListener {
            fun onFinished(refreshTokenResponse: RefreshTokenResponse?)

            fun onFailure(t: Throwable?)
        }

        fun getRefreshTokenResponse(
            onFinishedListener: OnFinishedListener?, jsonObject: JsonObject?
        )
    }

    interface View {
        fun showProgress()
        fun hideProgress()
        fun getRefreshTokenData(refreshTokenResponse: RefreshTokenResponse?)
        fun onResponseFailure(throwable: Throwable?)
    }

    interface Presenter {
        fun onDestroy()
        fun requestRefreshTokenFromServer(jsonObject: JsonObject?)
    }
}