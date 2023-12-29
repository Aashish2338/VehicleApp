package com.greenusys.vehicledealerapp.presenter

import com.greenusys.vehicledealerapp.contracts.VehicleDetailsContract
import com.greenusys.vehicledealerapp.models.VehicleDetailsResponse
import com.greenusys.vehicledealerapp.responseModelRepo.VehicleDetailsResponseModel

class VehicleDetailsPresenter(view: VehicleDetailsContract.View) : VehicleDetailsContract.Presenter,
    VehicleDetailsContract.Model.OnFinishedListener {

    private var vehicleDetailsContractView: VehicleDetailsContract.View? = view
    private var vehicleDetailsContractModel: VehicleDetailsContract.Model =
        VehicleDetailsResponseModel()

    override fun onDestroy() {
        this.vehicleDetailsContractView = null
    }

    override fun requestVehicleListDataFromServer(strToken: String?, strVehicleId: String?) {
        if (vehicleDetailsContractView != null) {
            vehicleDetailsContractView!!.showProgress()
        }
        vehicleDetailsContractModel.getVehicleDetailsResponse(this, strToken, strVehicleId)
    }

    override fun onFinished(vehicleDetailsResponse: VehicleDetailsResponse?) {
        if (vehicleDetailsContractView != null) {
            vehicleDetailsContractView!!.hideProgress()
        }
        vehicleDetailsContractView?.getVehicleDetailsData(vehicleDetailsResponse)
    }

    override fun onFailure(t: Throwable?) {
        if (vehicleDetailsContractView != null) {
            vehicleDetailsContractView?.hideProgress()
        }
        vehicleDetailsContractView?.onResponseFailure(t)
    }
}