package com.greenusys.vehicledealerapp.presenter

import com.greenusys.vehicledealerapp.contracts.AddVehicleContract
import com.greenusys.vehicledealerapp.models.VehicleResponse
import com.greenusys.vehicledealerapp.responseModelRepo.AddVehicleResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddVehiclePresenter(view: AddVehicleContract.View) : AddVehicleContract.Presenter,
    AddVehicleContract.Model.OnFinishedListener {

    private var addVehicleContractView: AddVehicleContract.View? = view
    private var addVehicleContractModel: AddVehicleContract.Model = AddVehicleResponseModel()

    override fun onFinished(vehicleResponse: VehicleResponse?) {
        if (addVehicleContractView != null) {
            addVehicleContractView!!.hideProgress()
        }
        addVehicleContractView?.getAddVehicleDetailsData(vehicleResponse)
    }

    override fun onFailure(t: Throwable?) {
        if (addVehicleContractView != null) {
            addVehicleContractView?.hideProgress()
        }
        addVehicleContractView?.onResponseFailure(t)
    }

    override fun onDestroy() {
        this.addVehicleContractView = null
    }

    override fun requestAddVehicleDetailsDataFromServer(
        strToken: String?,
        strVehicleNumber: RequestBody?,
        strHypotheticalRC: RequestBody?,
        strInsurance: RequestBody?,
        strOtherDocument: RequestBody?,
        strRcImage: MultipartBody.Part?,
        strBankNOCImage: MultipartBody.Part?,
        strInsuranceImage: MultipartBody.Part?,
        strOtherDocumentImage: MultipartBody.Part?
    ) {
        if (addVehicleContractView != null) {
            addVehicleContractView!!.showProgress()
        }
        addVehicleContractModel.getAddVehicleDetailsResponse(
            this,
            strToken,
            strVehicleNumber,
            strHypotheticalRC,
            strInsurance,
            strOtherDocument,
            strRcImage,
            strBankNOCImage,
            strInsuranceImage,
            strOtherDocumentImage
        )
    }
}