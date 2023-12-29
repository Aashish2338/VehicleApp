package com.greenusys.vehicledealerapp.contracts

import com.greenusys.vehicledealerapp.models.ProfileResponse

interface ProfileContract {
    interface Model {
        interface OnFinishedListener {
            fun onFinished(profileResponse: ProfileResponse?)

            fun onFailure(t: Throwable?)
        }

        fun getProfileDetailsResponse(
            onFinishedListener: OnFinishedListener?, strToken: String?, strUserId: String?
        )
    }

    interface View {
        fun showProgress()
        fun hideProgress()
        fun getProfileDetailsData(profileResponse: ProfileResponse?)
        fun onResponseFailure(throwable: Throwable?)
    }

    interface Presenter {
        fun onDestroy()
        fun requestProfileDetailsDataFromServer(strToken: String?, strUserId: String?)
    }
}