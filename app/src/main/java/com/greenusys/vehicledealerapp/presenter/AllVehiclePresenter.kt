package com.greenusys.vehicledealerapp.presenter

import com.greenusys.vehicledealerapp.contracts.AllVehicleListContract
import com.greenusys.vehicledealerapp.models.VehicleListResponse
import com.greenusys.vehicledealerapp.responseModelRepo.AllVehicleListResponseModel

class AllVehiclePresenter(view: AllVehicleListContract.View) : AllVehicleListContract.Presenter,
    AllVehicleListContract.Model.OnFinishedListener {

    private var allVehicleListContractView: AllVehicleListContract.View? = view
    private var allVehicleListContractModel: AllVehicleListContract.Model =
        AllVehicleListResponseModel()

    override fun onFinished(vehicleListResponse: VehicleListResponse?) {
        if (allVehicleListContractView != null) {
            allVehicleListContractView!!.hideProgress()
        }
        allVehicleListContractView?.getAllVehicleListData(vehicleListResponse)
    }

    override fun tokenExpired(code: Int) {
        if (allVehicleListContractView != null) {
            allVehicleListContractView!!.hideProgress()
        }
    }

    override fun onFailure(t: Throwable?) {
        if (allVehicleListContractView != null) {
            allVehicleListContractView?.hideProgress()
        }
        allVehicleListContractView?.onResponseFailure(t)
    }

    override fun onDestroy() {
        this.allVehicleListContractView = null
    }

    override fun requestAllVehicleListFromServer(strToken: String?) {
        if (allVehicleListContractView != null) {
            allVehicleListContractView!!.showProgress()
        }
        allVehicleListContractModel.getAllVehicleListResponse(this, strToken)
    }

    override fun newRefreshToken(code: Int) {
        if (allVehicleListContractView != null) {
            allVehicleListContractView?.hideProgress()
        }
    }
}