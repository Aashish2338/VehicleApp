package com.greenusys.vehicledealerapp.presenter

import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.contracts.TokenRefreshContract
import com.greenusys.vehicledealerapp.models.RefreshTokenResponse
import com.greenusys.vehicledealerapp.responseModelRepo.TokenRefreshResponseModel

class TokenRefreshPresenter(view: TokenRefreshContract.View) : TokenRefreshContract.Presenter,
    TokenRefreshContract.Model.OnFinishedListener {

    private var tokenRefreshContractView: TokenRefreshContract.View? = view
    private var tokenRefreshContractModel: TokenRefreshContract.Model = TokenRefreshResponseModel()

    override fun onFinished(refreshTokenResponse: RefreshTokenResponse?) {
        if (tokenRefreshContractView != null) {
            tokenRefreshContractView!!.hideProgress()
        }
        tokenRefreshContractView?.getRefreshTokenData(refreshTokenResponse)
    }

    override fun onFailure(t: Throwable?) {
        if (tokenRefreshContractView != null) {
            tokenRefreshContractView!!.hideProgress()
        }
        tokenRefreshContractView?.onResponseFailure(t)
    }

    override fun onDestroy() {
        this.tokenRefreshContractView = null
    }

    override fun requestRefreshTokenFromServer(jsonObject: JsonObject?) {
        if (tokenRefreshContractView != null) {
            tokenRefreshContractView!!.showProgress()
        }
        tokenRefreshContractModel.getRefreshTokenResponse(this, jsonObject)
    }
}