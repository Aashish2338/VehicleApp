package com.greenusys.vehicledealerapp.responseModelRepo

import android.util.Log
import com.greenusys.vehicledealerapp.contracts.DeleteVehicleContract
import com.greenusys.vehicledealerapp.models.ResendOtpResponse
import com.greenusys.vehicledealerapp.network.ApiClients
import com.greenusys.vehicledealerapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteVehicleResponseModel : DeleteVehicleContract.Model {

    private val TAG = "DeleteVehicleResponseModel"

    override fun getDeleteVehicleResponse(
        onFinishedListener: DeleteVehicleContract.Model.OnFinishedListener?,
        strToken: String?,
        strId: String?
    ) {
        try {
            val apiService: ApiInterface =
                ApiClients.getClient()?.create<ApiInterface>(ApiInterface::class.java)!!
            val call: Call<ResendOtpResponse?>? = apiService.getDeleteVehicleData(strToken, strId)
            call!!.enqueue(object : Callback<ResendOtpResponse?> {
                override fun onResponse(
                    call: Call<ResendOtpResponse?>, response: Response<ResendOtpResponse?>
                ) {
                    if (response.isSuccessful) {
                        var otpResponse = response.body()
                        Log.d(TAG, "Delete Vehicle Response received: $otpResponse")
                        onFinishedListener?.onFinished(otpResponse)
                    }
                }

                override fun onFailure(call: Call<ResendOtpResponse?>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    onFinishedListener?.onFailure(t)
                }
            })
        } catch (exp: Exception) {
            println("Delete Vehicle otp error :- ${exp.message}")
            exp.stackTrace
        }
    }
}