package com.greenusys.vehicledealerapp.presenter

import com.greenusys.vehicledealerapp.contracts.ResendOtpContract
import com.greenusys.vehicledealerapp.models.ResendOtpResponse
import com.greenusys.vehicledealerapp.responseModelRepo.ResendResponseModel

class ResendOtpPresenter(view: ResendOtpContract.View) : ResendOtpContract.Presenter,
    ResendOtpContract.Model.OnFinishedListener {

    private var resendOtpContractView: ResendOtpContract.View? = view
    private var resendResponseModel: ResendOtpContract.Model = ResendResponseModel()

    override fun onFinished(resendOtpResponse: ResendOtpResponse?) {
        if (resendOtpContractView != null) {
            resendOtpContractView!!.hideProgress()
        }
        resendOtpContractView?.getResendOtpData(resendOtpResponse)
    }

    override fun onFailure(t: Throwable?) {
        if (resendOtpContractView != null) {
            resendOtpContractView?.hideProgress()
        }
        resendOtpContractView?.onResponseFailure(t)
    }

    override fun onDestroy() {
        this.resendOtpContractView = null
    }

    override fun requestResendOtpDataFromServer(strId: String?) {
        if (resendOtpContractView != null) {
            resendOtpContractView!!.showProgress()
        }
        resendResponseModel.getResendOtpResponse(this, strId)
    }
}