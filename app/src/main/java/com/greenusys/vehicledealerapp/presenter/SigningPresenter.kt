package com.greenusys.vehicledealerapp.presenter

import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.contracts.UserSigningContract
import com.greenusys.vehicledealerapp.responseModelRepo.UserSigningResponseModel
import com.greenusys.vehicledealerapp.models.SigningResponse

class SigningPresenter(view: UserSigningContract.View) : UserSigningContract.Presenter,
    UserSigningContract.Model.OnFinishedListener {

    private var userSigningContractView: UserSigningContract.View? = view
    private var userSigningContractModel: UserSigningContract.Model =
        UserSigningResponseModel()

    override fun onDestroy() {
        this.userSigningContractView = null
    }

    override fun requestDataFromServer(jsonObject: JsonObject?) {
        if (userSigningContractView != null) {
            userSigningContractView!!.showProgress()
        }
        userSigningContractModel.getSignResponse(this, jsonObject)
    }

    override fun onFinished(signingResponse: SigningResponse?) {
        if (userSigningContractView != null) {
            userSigningContractView!!.hideProgress()
        }
        userSigningContractView?.getLoginUserData(signingResponse)
    }

    override fun onFailure(t: Throwable?) {
        if (userSigningContractView != null) {
            userSigningContractView!!.hideProgress()
        }
        userSigningContractView?.onResponseFailure(t)
    }
}