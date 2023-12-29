package com.greenusys.vehicledealerapp.presenter

import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.contracts.OtpContract
import com.greenusys.vehicledealerapp.models.OtpResponse
import com.greenusys.vehicledealerapp.responseModelRepo.OtpResponseModel

class OtpPresenter(view: OtpContract.View) : OtpContract.Presenter,
    OtpContract.Model.OnFinishedListener {

    private var otpContractView: OtpContract.View? = view
    private var otpContractModel: OtpContract.Model = OtpResponseModel()

    override fun onFinished(otpResponse: OtpResponse?) {
        if (otpContractView != null) {
            otpContractView!!.hideProgress()
        }
        otpContractView?.getLoginOtpData(otpResponse)
    }

    override fun onFailure(t: Throwable?) {
        if (otpContractView != null) {
            otpContractView?.hideProgress()
        }
        otpContractView?.onResponseFailure(t)
    }

    override fun onDestroy() {
        this.otpContractView = null
    }

    override fun requestOtpDataFromServer(jsonObject: JsonObject?) {
        if (otpContractView != null) {
            otpContractView!!.showProgress()
        }
        otpContractModel.getOtpResponse(this, jsonObject)
    }
}