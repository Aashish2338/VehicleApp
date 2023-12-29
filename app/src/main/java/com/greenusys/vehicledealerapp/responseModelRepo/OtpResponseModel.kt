package com.greenusys.vehicledealerapp.responseModelRepo

import android.util.Log
import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.contracts.OtpContract
import com.greenusys.vehicledealerapp.models.OtpResponse
import com.greenusys.vehicledealerapp.network.ApiClients
import com.greenusys.vehicledealerapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpResponseModel : OtpContract.Model {

    private val TAG = "OtpResponseModel"

    override fun getOtpResponse(
        onFinishedListener: OtpContract.Model.OnFinishedListener?,
        jsonObject: JsonObject?
    ) {
        try {
            val apiService: ApiInterface =
                ApiClients.getClient()?.create<ApiInterface>(ApiInterface::class.java)!!
            val call: Call<OtpResponse?>? = apiService.getVerifyOtpData(jsonObject)
            call!!.enqueue(object : Callback<OtpResponse?> {
                override fun onResponse(
                    call: Call<OtpResponse?>, response: Response<OtpResponse?>
                ) {
                    if (response.isSuccessful) {
                        var otpResponse = response.body()
                        Log.d(TAG, "Otp Response received: $otpResponse")
                        onFinishedListener?.onFinished(otpResponse)
                    }
                }

                override fun onFailure(call: Call<OtpResponse?>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    onFinishedListener?.onFailure(t);
                }
            })
        } catch (exp: Exception) {
            println("Login otp error :- ${exp.message}")
            exp.stackTrace
        }
    }
}