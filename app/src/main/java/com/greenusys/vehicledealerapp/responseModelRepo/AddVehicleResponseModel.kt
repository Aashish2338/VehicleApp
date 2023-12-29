package com.greenusys.vehicledealerapp.responseModelRepo

import android.util.Log
import com.greenusys.vehicledealerapp.contracts.AddVehicleContract
import com.greenusys.vehicledealerapp.models.VehicleResponse
import com.greenusys.vehicledealerapp.network.ApiClients
import com.greenusys.vehicledealerapp.network.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddVehicleResponseModel : AddVehicleContract.Model {

    private val TAG = "Add vehicle response model"

    override fun getAddVehicleDetailsResponse(
        onFinishedListener: AddVehicleContract.Model.OnFinishedListener?,
        strToken: String?,
        strVehicleNumber: RequestBody?,
        strHypotheticalRC: RequestBody?,
        strInsurance: RequestBody?,
        strOtherDocument: RequestBody?,
        strRcImage: MultipartBody.Part?,
        strBankNOCImage: MultipartBody.Part?,
        strInsuranceImage: MultipartBody.Part?,
        strOtherDocumentImage: MultipartBody.Part?
    ) {
        try {
            val apiService: ApiInterface =
                ApiClients.getClient()?.create<ApiInterface>(ApiInterface::class.java)!!
            if (strRcImage != null && strBankNOCImage != null && strInsuranceImage != null && strOtherDocumentImage != null) {
                val call: Call<VehicleResponse?>? = apiService.getAddVehicleA(
                    strToken,
                    strVehicleNumber,
                    strHypotheticalRC,
                    strInsurance,
                    strOtherDocument,
                    strRcImage,
                    strBankNOCImage,
                    strInsuranceImage,
                    strOtherDocumentImage
                )
                call!!.enqueue(object : Callback<VehicleResponse?> {
                    override fun onResponse(
                        call: Call<VehicleResponse?>, response: Response<VehicleResponse?>
                    ) {
                        println("Api hit of C Interface")
                        if (response.isSuccessful) {
                            var otpResponse = response.body()
                            Log.d(TAG, "Add vehicle response received: $otpResponse")
                            onFinishedListener?.onFinished(otpResponse)
                        }
                    }

                    override fun onFailure(call: Call<VehicleResponse?>, t: Throwable) {
                        Log.e(TAG, t.toString())
                        onFinishedListener?.onFailure(t)
                    }
                })
            } else if (strRcImage != null && strBankNOCImage != null && strInsuranceImage != null) {
                val call: Call<VehicleResponse?>? = apiService.getAddVehicleB(
                    strToken,
                    strVehicleNumber,
                    strHypotheticalRC,
                    strInsurance,
                    strOtherDocument,
                    strRcImage,
                    strBankNOCImage,
                    strInsuranceImage
                )
                call!!.enqueue(object : Callback<VehicleResponse?> {
                    override fun onResponse(
                        call: Call<VehicleResponse?>, response: Response<VehicleResponse?>
                    ) {
                        println("Api hit of D Interface")
                        if (response.isSuccessful) {
                            var otpResponse = response.body()
                            Log.d(TAG, "Add vehicle response received: $otpResponse")
                            onFinishedListener?.onFinished(otpResponse)
                        }
                    }

                    override fun onFailure(call: Call<VehicleResponse?>, t: Throwable) {
                        Log.e(TAG, t.toString())
                        onFinishedListener?.onFailure(t)
                    }
                })
            } else if (strRcImage != null && strBankNOCImage != null) {
                val call: Call<VehicleResponse?>? = apiService.getAddVehicleC(
                    strToken,
                    strVehicleNumber,
                    strHypotheticalRC,
                    strInsurance,
                    strOtherDocument,
                    strRcImage,
                    strBankNOCImage
                )
                call!!.enqueue(object : Callback<VehicleResponse?> {
                    override fun onResponse(
                        call: Call<VehicleResponse?>, response: Response<VehicleResponse?>
                    ) {
                        println("Api hit of B Interface")
                        if (response.isSuccessful) {
                            var otpResponse = response.body()
                            Log.d(TAG, "Add vehicle response received: $otpResponse")
                            onFinishedListener?.onFinished(otpResponse)
                        }
                    }

                    override fun onFailure(call: Call<VehicleResponse?>, t: Throwable) {
                        Log.e(TAG, t.toString())
                        onFinishedListener?.onFailure(t)
                    }
                })
            } else if (strRcImage != null && strInsuranceImage != null) {
                val call: Call<VehicleResponse?>? = apiService.getAddVehicleD(
                    strToken,
                    strVehicleNumber,
                    strHypotheticalRC,
                    strInsurance,
                    strOtherDocument,
                    strRcImage,
                    strInsuranceImage
                )
                call!!.enqueue(object : Callback<VehicleResponse?> {
                    override fun onResponse(
                        call: Call<VehicleResponse?>, response: Response<VehicleResponse?>
                    ) {
                        println("Api hit of A Interface")
                        if (response.isSuccessful) {
                            var otpResponse = response.body()
                            Log.d(TAG, "Add vehicle response received: $otpResponse")
                            onFinishedListener?.onFinished(otpResponse)
                        }
                    }

                    override fun onFailure(call: Call<VehicleResponse?>, t: Throwable) {
                        Log.e(TAG, t.toString())
                        onFinishedListener?.onFailure(t)
                    }
                })
            } else if (strRcImage != null && strOtherDocumentImage != null) {
                val call: Call<VehicleResponse?>? = apiService.getAddVehicleE(
                    strToken,
                    strVehicleNumber,
                    strHypotheticalRC,
                    strInsurance,
                    strOtherDocument,
                    strRcImage,
                    strOtherDocumentImage
                )
                call!!.enqueue(object : Callback<VehicleResponse?> {
                    override fun onResponse(
                        call: Call<VehicleResponse?>, response: Response<VehicleResponse?>
                    ) {
                        println("Api hit of A Interface")
                        if (response.isSuccessful) {
                            var otpResponse = response.body()
                            Log.d(TAG, "Add vehicle response received: $otpResponse")
                            onFinishedListener?.onFinished(otpResponse)
                        }
                    }

                    override fun onFailure(call: Call<VehicleResponse?>, t: Throwable) {
                        Log.e(TAG, t.toString())
                        onFinishedListener?.onFailure(t)
                    }
                })
            } else {
                val call: Call<VehicleResponse?>? = apiService.getAddVehicle(
                    strToken,
                    strVehicleNumber,
                    strHypotheticalRC,
                    strInsurance,
                    strOtherDocument,
                    strRcImage
                )
                call!!.enqueue(object : Callback<VehicleResponse?> {
                    override fun onResponse(
                        call: Call<VehicleResponse?>, response: Response<VehicleResponse?>
                    ) {
                        println("Api hit of Interface")
                        if (response.isSuccessful) {
                            var otpResponse = response.body()
                            Log.d(TAG, "Add vehicle response received: $otpResponse")
                            onFinishedListener?.onFinished(otpResponse)
                        }
                    }

                    override fun onFailure(call: Call<VehicleResponse?>, t: Throwable) {
                        Log.e(TAG, t.toString())
                        onFinishedListener?.onFailure(t)
                    }
                })
            }
        } catch (exp: Exception) {
            println("Add vehicle data error :- ${exp.message}")
            exp.stackTrace
        }
    }
}