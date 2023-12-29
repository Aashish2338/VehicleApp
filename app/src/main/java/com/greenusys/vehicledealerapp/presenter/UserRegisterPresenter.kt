package com.greenusys.vehicledealerapp.presenter

import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.contracts.UserRegisterContract
import com.greenusys.vehicledealerapp.models.UserRegisterResponse
import com.greenusys.vehicledealerapp.responseModelRepo.UserRegisterResponseModel

class UserRegisterPresenter(view: UserRegisterContract.View) : UserRegisterContract.Presenter,
    UserRegisterContract.Model.OnFinishedListener {

    private var userRegisterContractView: UserRegisterContract.View? = view
    private var userRegisterContractModel: UserRegisterContract.Model = UserRegisterResponseModel()

    override fun onFinished(userRegisterResponse: UserRegisterResponse?) {
        if (userRegisterContractView != null) {
            userRegisterContractView!!.hideProgress()
        }
        userRegisterContractView?.getRegisterData(userRegisterResponse)
    }

    override fun onFailure(t: Throwable?) {
        if (userRegisterContractView != null) {
            userRegisterContractView?.hideProgress()
        }
        userRegisterContractView?.onResponseFailure(t)
    }

    override fun onDestroy() {
        this.userRegisterContractView = null
    }

    override fun requestRegisterDataFromServer(jsonObject: JsonObject?) {
        if (userRegisterContractView != null) {
            userRegisterContractView!!.showProgress()
        }
        userRegisterContractModel.getRegisterResponse(this, jsonObject)
    }
}