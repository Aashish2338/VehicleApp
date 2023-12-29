package com.greenusys.vehicledealerapp.responseModelRepo

import android.util.Log
import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.contracts.TokenRefreshContract
import com.greenusys.vehicledealerapp.models.RefreshTokenResponse
import com.greenusys.vehicledealerapp.network.ApiClients
import com.greenusys.vehicledealerapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenRefreshResponseModel : TokenRefreshContract.Model {

    private val TAG = "TokenRefreshResponseModel"

    override fun getRefreshTokenResponse(
        onFinishedListener: TokenRefreshContract.Model.OnFinishedListener?, jsonObject: JsonObject?
    ) {
        try {
            val apiService: ApiInterface =
                ApiClients.getClient()?.create<ApiInterface>(ApiInterface::class.java)!!
            val call: Call<RefreshTokenResponse?>? = apiService.getRefreshToken(jsonObject)
            call!!.enqueue(object : Callback<RefreshTokenResponse?> {
                override fun onResponse(
                    call: Call<RefreshTokenResponse?>, response: Response<RefreshTokenResponse?>
                ) {
                    if (response.isSuccessful) {
                        var signingResponse = response.body()
                        Log.d(TAG, "Refresh token response received: $signingResponse")
                        onFinishedListener?.onFinished(signingResponse)
                    }
                }

                override fun onFailure(call: Call<RefreshTokenResponse?>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    onFinishedListener?.onFailure(t);
                }
            })
        } catch (exp: Exception) {
            println("Refresh token error :- ${exp.message}")
            exp.stackTrace
        }
    }
}