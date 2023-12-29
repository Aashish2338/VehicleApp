package com.greenusys.vehicledealerapp.responseModelRepo

import android.util.Log
import android.widget.Toast
import com.greenusys.vehicledealerapp.contracts.AllVehicleListContract
import com.greenusys.vehicledealerapp.models.VehicleListResponse
import com.greenusys.vehicledealerapp.network.ApiClients
import com.greenusys.vehicledealerapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllVehicleListResponseModel : AllVehicleListContract.Model {

    private val TAG = "AllVehicleListResponseModel"

    override fun getAllVehicleListResponse(
        onFinishedListener: AllVehicleListContract.Model.OnFinishedListener?, strToken: String?
    ) {
        try {
            val apiService: ApiInterface =
                ApiClients.getClient()?.create<ApiInterface>(ApiInterface::class.java)!!
            val call: Call<VehicleListResponse?>? = apiService.getAllVehicleData(strToken)
            call!!.enqueue(object : Callback<VehicleListResponse?> {
                override fun onResponse(
                    call: Call<VehicleListResponse?>, response: Response<VehicleListResponse?>
                ) {
                    if (response.isSuccessful) {
                        val otpResponse = response.body()
                        Log.d(TAG, "Vehicle Details Response received: $otpResponse")
                        onFinishedListener?.onFinished(otpResponse)
                    } else {
                        when (response.code()) {
                            500 -> {
                                println("Response code :- 500")
                                onFinishedListener?.tokenExpired(500)
                            }

                            401 -> {
                                println("Response code :- 401")
                                onFinishedListener?.tokenExpired(401)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<VehicleListResponse?>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    onFinishedListener?.onFailure(t)
                }
            })
        } catch (exp: Exception) {
            println("Vehicle list data error :- ${exp.message}")
            exp.stackTrace
        }
    }
}