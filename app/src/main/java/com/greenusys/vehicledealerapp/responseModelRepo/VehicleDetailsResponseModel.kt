package com.greenusys.vehicledealerapp.responseModelRepo

import android.util.Log
import com.greenusys.vehicledealerapp.contracts.VehicleDetailsContract
import com.greenusys.vehicledealerapp.models.VehicleDetailsResponse
import com.greenusys.vehicledealerapp.network.ApiClients
import com.greenusys.vehicledealerapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VehicleDetailsResponseModel : VehicleDetailsContract.Model {

    private val TAG = "VehicleDetailsResponseModel"

    override fun getVehicleDetailsResponse(
        onFinishedListener: VehicleDetailsContract.Model.OnFinishedListener?,
        strToken: String?,
        strVehicleId: String?
    ) {
        try {
            val apiService: ApiInterface =
                ApiClients.getClient()?.create<ApiInterface>(ApiInterface::class.java)!!
            val call: Call<VehicleDetailsResponse?>? =
                apiService.getVehicleDetailsData(strToken, strVehicleId)
            call!!.enqueue(object : Callback<VehicleDetailsResponse?> {
                override fun onResponse(
                    call: Call<VehicleDetailsResponse?>, response: Response<VehicleDetailsResponse?>
                ) {
                    if (response.isSuccessful) {
                        var otpResponse = response.body()
                        Log.d(TAG, "Vehicle Details Response received: $otpResponse")
                        onFinishedListener?.onFinished(otpResponse)
                    }
                }

                override fun onFailure(call: Call<VehicleDetailsResponse?>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    onFinishedListener?.onFailure(t)
                }
            })
        } catch (exp: Exception) {
            println("Vehicle details data error :- ${exp.message}")
            exp.stackTrace
        }
    }
}