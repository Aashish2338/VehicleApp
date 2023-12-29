package com.greenusys.vehicledealerapp.presenter

import com.greenusys.vehicledealerapp.contracts.ProfileContract
import com.greenusys.vehicledealerapp.models.ProfileResponse
import com.greenusys.vehicledealerapp.responseModelRepo.ProfileResponseModel

class ProfilePresenter(view: ProfileContract.View) : ProfileContract.Presenter,
    ProfileContract.Model.OnFinishedListener {

    private var profileContractView: ProfileContract.View? = view
    private var profileContractModel: ProfileContract.Model = ProfileResponseModel()

    override fun onFinished(profileResponse: ProfileResponse?) {
        if (profileContractView != null) {
            profileContractView!!.hideProgress()
        }
        profileContractView?.getProfileDetailsData(profileResponse)
    }

    override fun onFailure(t: Throwable?) {
        if (profileContractView != null) {
            profileContractView?.hideProgress()
        }
        profileContractView?.onResponseFailure(t)
    }

    override fun onDestroy() {
        this.profileContractView = null
    }

    override fun requestProfileDetailsDataFromServer(strToken: String?, strUserId: String?) {
        if (profileContractView != null) {
            profileContractView!!.showProgress()
        }
        profileContractModel.getProfileDetailsResponse(this, strToken, strUserId)
    }
}