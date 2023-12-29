package com.greenusys.vehicledealerapp.responseModelRepo

import android.util.Log
import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.contracts.UserSigningContract
import com.greenusys.vehicledealerapp.models.SigningResponse
import com.greenusys.vehicledealerapp.network.ApiClients
import com.greenusys.vehicledealerapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSigningResponseModel : UserSigningContract.Model {

    private val TAG = "SignResponseModel"

    override fun getSignResponse(
        onFinishedListener: UserSigningContract.Model.OnFinishedListener?,
        jsonObject: JsonObject?
    ) {
        try {
            val apiService: ApiInterface =
                ApiClients.getClient()?.create<ApiInterface>(ApiInterface::class.java)!!
            val call: Call<SigningResponse?>? = apiService.getLoginData(jsonObject)
            call!!.enqueue(object : Callback<SigningResponse?> {
                override fun onResponse(
                    call: Call<SigningResponse?>, response: Response<SigningResponse?>
                ) {
                    if (response.isSuccessful) {
                        var signingResponse = response.body()
                        Log.d(TAG, "Signing Response received: $signingResponse")
                        onFinishedListener?.onFinished(signingResponse)
                    }
                }

                override fun onFailure(call: Call<SigningResponse?>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    onFinishedListener?.onFailure(t)
                }
            })
        } catch (exp: Exception) {
            println("Login error :- ${exp.message}")
            exp.stackTrace
        }
    }
}