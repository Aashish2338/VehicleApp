package com.greenusys.vehicledealerapp.presenter

import com.greenusys.vehicledealerapp.contracts.DeleteVehicleContract
import com.greenusys.vehicledealerapp.models.ResendOtpResponse
import com.greenusys.vehicledealerapp.responseModelRepo.DeleteVehicleResponseModel

class DeleteVehiclePresenter(view: DeleteVehicleContract.View) : DeleteVehicleContract.Presenter,
    DeleteVehicleContract.Model.OnFinishedListener {

    private var deleteVehicleContractView: DeleteVehicleContract.View? = view
    private var deleteVehicleContractModel: DeleteVehicleContract.Model =
        DeleteVehicleResponseModel()

    override fun onFinished(resendOtpResponse: ResendOtpResponse?) {
        if (deleteVehicleContractView != null) {
            deleteVehicleContractView!!.hideProgress()
        }
        deleteVehicleContractView?.getDeleteVehicleData(resendOtpResponse)
    }

    override fun onFailure(t: Throwable?) {
        if (deleteVehicleContractView != null) {
            deleteVehicleContractView?.hideProgress()
        }
        deleteVehicleContractView?.onResponseFailure(t)
    }

    override fun onDestroy() {
        this.deleteVehicleContractView = null
    }

    override fun requestDeleteVehicleDataFromServer(strToken: String?, strId: String?) {
        if (deleteVehicleContractView != null) {
            deleteVehicleContractView!!.showProgress()
        }
        deleteVehicleContractModel.getDeleteVehicleResponse(this, strToken, strId)
    }
}