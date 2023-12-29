package com.greenusys.vehicledealerapp.responseModelRepo

import android.util.Log
import com.greenusys.vehicledealerapp.contracts.ProfileContract
import com.greenusys.vehicledealerapp.models.ProfileResponse
import com.greenusys.vehicledealerapp.network.ApiClients
import com.greenusys.vehicledealerapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileResponseModel : ProfileContract.Model {

    private val TAG = "ProfileResponseModel"

    override fun getProfileDetailsResponse(
        onFinishedListener: ProfileContract.Model.OnFinishedListener?,
        strToken: String?,
        strUserId: String?
    ) {
        try {
            val apiService: ApiInterface =
                ApiClients.getClient()?.create<ApiInterface>(ApiInterface::class.java)!!
            val call: Call<ProfileResponse?>? = apiService.getUserProfileData(strToken, strUserId)
            call!!.enqueue(object : Callback<ProfileResponse?> {
                override fun onResponse(
                    call: Call<ProfileResponse?>, response: Response<ProfileResponse?>
                ) {
                    if (response.isSuccessful) {
                        var otpResponse = response.body()
                        Log.d(TAG, "Profile Details Response received: $otpResponse")
                        onFinishedListener?.onFinished(otpResponse)
                    }
                }

                override fun onFailure(call: Call<ProfileResponse?>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    onFinishedListener?.onFailure(t)
                }
            })
        } catch (exp: Exception) {
            println("Profile details data error :- ${exp.message}")
            exp.stackTrace
        }
    }
}