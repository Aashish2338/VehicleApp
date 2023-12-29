package com.greenusys.vehicledealerapp.responseModelRepo

import android.util.Log
import com.greenusys.vehicledealerapp.contracts.ResendOtpContract
import com.greenusys.vehicledealerapp.models.ResendOtpResponse
import com.greenusys.vehicledealerapp.network.ApiClients
import com.greenusys.vehicledealerapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResendResponseModel : ResendOtpContract.Model {

    private val TAG = "ResendResponseModel"

    override fun getResendOtpResponse(
        onFinishedListener: ResendOtpContract.Model.OnFinishedListener?, strId: String?
    ) {
        try {
            val apiService: ApiInterface =
                ApiClients.getClient()?.create<ApiInterface>(ApiInterface::class.java)!!
            val call: Call<ResendOtpResponse?>? = apiService.getResendOtpData(strId)
            call!!.enqueue(object : Callback<ResendOtpResponse?> {
                override fun onResponse(
                    call: Call<ResendOtpResponse?>,
                    response: Response<ResendOtpResponse?>
                ) {
                    if (response.isSuccessful) {
                        var otpResponse = response.body()
                        Log.d(TAG, "Resend Otp Response received: $otpResponse")
                        onFinishedListener?.onFinished(otpResponse)
                    }
                }

                override fun onFailure(call: Call<ResendOtpResponse?>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    onFinishedListener?.onFailure(t)
                }
            })
        } catch (exp: Exception) {
            println("Login resend otp error :- ${exp.message}")
            exp.stackTrace
        }
    }
}