package com.greenusys.vehicledealerapp.responseModelRepo

import android.util.Log
import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.contracts.UserRegisterContract
import com.greenusys.vehicledealerapp.models.UserRegisterResponse
import com.greenusys.vehicledealerapp.network.ApiClients
import com.greenusys.vehicledealerapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRegisterResponseModel : UserRegisterContract.Model {

    private val TAG = "UserRegisterResponseModel"

    override fun getRegisterResponse(
        onFinishedListener: UserRegisterContract.Model.OnFinishedListener?,
        jsonObject: JsonObject?
    ) {
        try {
            val apiService: ApiInterface =
                ApiClients.getClient()?.create<ApiInterface>(ApiInterface::class.java)!!
            val call: Call<UserRegisterResponse?>? =
                apiService.getUserRegisterData(jsonObject)
            call!!.enqueue(object : Callback<UserRegisterResponse?> {
                override fun onResponse(
                    call: Call<UserRegisterResponse?>, response: Response<UserRegisterResponse?>
                ) {
                    if (response.isSuccessful) {
                        var otpResponse = response.body()
                        Log.d(TAG, "Register Response received: $otpResponse")
                        onFinishedListener?.onFinished(otpResponse)
                    }
                }

                override fun onFailure(call: Call<UserRegisterResponse?>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    println("Register error data :- ${t.cause}")
                    onFinishedListener?.onFailure(t);
                }
            })
        } catch (exp: Exception) {
            println("Register error :- ${exp.message}")
            exp.stackTrace
        }
    }
}