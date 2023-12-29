package com.greenusys.vehicledealerapp.activities

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.greenusys.vehicledealerapp.R
import com.greenusys.vehicledealerapp.adapters.DealerListAdapter
import com.greenusys.vehicledealerapp.contracts.TokenRefreshContract
import com.greenusys.vehicledealerapp.models.RefreshTokenResponse
import com.greenusys.vehicledealerapp.models.VehicleListResponse
import com.greenusys.vehicledealerapp.models.VehiclesData
import com.greenusys.vehicledealerapp.network.ApiClients
import com.greenusys.vehicledealerapp.network.ApiInterface
import com.greenusys.vehicledealerapp.presenter.TokenRefreshPresenter
import com.greenusys.vehicledealerapp.utilities.UserSession
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity(), TokenRefreshContract.View, View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var userSession: UserSession
    private lateinit var dealerName_Tv: TextView
    private lateinit var logout_Img: ImageView
    private lateinit var addVehicle_btn: AppCompatButton
    private lateinit var dealersListRcView: RecyclerView
    private lateinit var oopsDataLayout: LinearLayout
    private lateinit var dealerListAdapter: DealerListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var vehicles: List<VehiclesData>? = null
    private lateinit var tokenRefreshPresenter: TokenRefreshPresenter
    private lateinit var dialog: Dialog
    private var strId: String? = ""
    private var tokenKey: String? = ""
    private var refreshTokenKey: String? = ""
    private var strName: String? = ""
    private var strMobileNumber: String? = ""
    private var strCity: String? = ""

    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        try {
            mContext = this
            userSession = UserSession(mContext)
            tokenRefreshPresenter = TokenRefreshPresenter(this)
            getLayoutViewIdFind()

            addVehicle_btn.setOnClickListener(this)
            logout_Img.setOnClickListener(this)
            dealerName_Tv.setOnClickListener(this)

            oopsDataLayout.visibility = View.GONE

            tokenKey = "Bearer " + userSession.getServiceKeyToken()
            refreshTokenKey = userSession.getRefreshToken()

            strId = userSession.getUserId()
            strName = userSession.getUserName()
            strMobileNumber = userSession.getUserMobile()
            strCity = userSession.getUserCity()
            println("Token Key value :- $tokenKey, $strId, $strName, $strMobileNumber, $strCity")
            println("Refresh Token Key value :- $refreshTokenKey")
            dealerName_Tv.text = strName

            if (intent.extras != null) {
                tokenKey = "Bearer " + intent.extras?.getString("tokenKey")
                println("Dashboard Page B :- $tokenKey")
                getAllVehicleData(tokenKey)
            } else {
                tokenKey = "Bearers " + userSession.getServiceKeyToken()
                println("Dashboard Page C :- $tokenKey")
                getAllVehicleData(tokenKey)
            }
        } catch (exp: Exception) {
            println("Dashboard page error :- ${exp.stackTrace}")
            exp.stackTrace
        }
    }

    private fun getAllVehicleData(tokenKey: String?) {
        try {
            dialog = Dialog(mContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.google_progress_bar_item)
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()

            val apiService: ApiInterface =
                ApiClients.getClient()?.create<ApiInterface>(ApiInterface::class.java)!!
            val call: Call<VehicleListResponse?>? = apiService.getAllVehicleData(tokenKey)
            call!!.enqueue(object : Callback<VehicleListResponse?> {
                override fun onResponse(
                    call: Call<VehicleListResponse?>, response: Response<VehicleListResponse?>
                ) {
                    if (response.isSuccessful) {
                        if (dialog.isShowing) {
                            dialog.cancel()
                            dialog.dismiss()
                        }
                        val otpResponse = response.body()
                        println("Vehicle Details Response received: $otpResponse")
                        vehicles = response.body()?.vehicles
                        if (vehicles?.size != 0) {
                            setAllDataInRecycler(vehicles)
                        } else {
                            println("Vehicle Details Response received!")
                        }
                    } else {
                        if (dialog.isShowing) {
                            dialog.cancel()
                            dialog.dismiss()
                        }
                        when (response.code()) {
                            500 -> {
                                println("Response code :- 500, means internal server error so please contact your admin department.")
//                                val strToken: String = apiJsonToken(refreshTokenKey).toString()
//                                println("Refresh token key value :- $strToken")
//                                tokenRefreshPresenter.requestRefreshTokenFromServer(
//                                    apiJsonToken(
//                                        refreshTokenKey
//                                    )
//                                )
                            }

                            401 -> {
                                println("Response code :- 401")
                                val strToken: String = apiJsonToken(refreshTokenKey).toString()
                                println("Refresh token key value :- $strToken")
                                tokenRefreshPresenter.requestRefreshTokenFromServer(
                                    apiJsonToken(
                                        refreshTokenKey
                                    )
                                )
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<VehicleListResponse?>, t: Throwable) {
                    println("failure data :- ${t.message}")
                    if (dialog.isShowing) {
                        dialog.cancel()
                        dialog.dismiss()
                    }
                }
            })
        } catch (exp: Exception) {
            if (dialog.isShowing) {
                dialog.cancel()
                dialog.dismiss()
            }
            println("Vehicle list data error :- ${exp.message}")
            exp.stackTrace
        }
    }

    private fun setAllDataInRecycler(vehicles: List<VehiclesData>?) {
        try {
            if (vehicles!!.isNotEmpty()) {
                if (vehicles?.size!! >= 0) {
                    oopsDataLayout.visibility = View.GONE
                    dealersListRcView.visibility = View.VISIBLE
                    dealerListAdapter = DealerListAdapter(mContext, vehicles)
                    linearLayoutManager = LinearLayoutManager(mContext)
                    dealersListRcView.layoutManager = linearLayoutManager
                    dealersListRcView.adapter = dealerListAdapter
                } else {
                    oopsDataLayout.visibility = View.VISIBLE
                    dealersListRcView.visibility = View.GONE
                }
            } else {
                oopsDataLayout.visibility = View.VISIBLE
                dealersListRcView.visibility = View.GONE
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    private fun getLayoutViewIdFind() {
        try {
            dealerName_Tv = findViewById<TextView>(R.id.dealerName_Tv) as TextView
            logout_Img = findViewById<ImageView>(R.id.logout_Img) as ImageView
            addVehicle_btn = findViewById<AppCompatButton>(R.id.addVehicle_btn) as AppCompatButton
            dealersListRcView = findViewById<RecyclerView>(R.id.dealersListRcView) as RecyclerView
            oopsDataLayout = findViewById<LinearLayout>(R.id.oopsDataLayout) as LinearLayout

        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.addVehicle_btn -> {
                startActivity(
                    Intent(
                        mContext, AddVehicleDetailsActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }

            R.id.logout_Img -> {
                try {
                    val alert = AlertDialog.Builder(mContext)
                    alert.setTitle("Logout")
                    alert.setMessage("Are you sure you want to close this App?")
                    alert.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                        userSession.logoutUser()
                        userSession.setOtpCode("")
                        Toast.makeText(
                            mContext,
                            "You have been successfully log out of the system!",
                            Toast.LENGTH_SHORT
                        ).show()
                        this@DashboardActivity.finish()
                    })

                    alert.setNegativeButton("No", DialogInterface.OnClickListener { _, _ ->
                        Toast.makeText(mContext, "Thanks for staying here!", Toast.LENGTH_SHORT)
                            .show()
                    })

                    val alertDialog = alert.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).isAllCaps = false
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).isAllCaps = false
                } catch (exp: Exception) {
                    exp.stackTrace
                }
            }

            R.id.dealerName_Tv -> {
                startActivity(
                    Intent(
                        mContext, UserProfileActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

    override fun showProgress() {
        dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.google_progress_bar_item)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun hideProgress() {
        dialog.cancel()
        dialog.dismiss()
    }

    override fun getRefreshTokenData(refreshTokenResponse: RefreshTokenResponse?) {
        if (refreshTokenResponse?.accessToken != null) {
            tokenKey = "Bearer " + refreshTokenResponse.accessToken
            println("Dashboard Page A :- $tokenKey")
            userSession.setServiceKeyToken(refreshTokenResponse.accessToken)
            getAllVehicleData(tokenKey)
//            allVehiclePresenter.requestAllVehicleListFromServer(
//                tokenKey
//            )
        } else {
            Toast.makeText(
                mContext, "Something went wrong so, please try after some time!", Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResponseFailure(throwable: Throwable?) {
        Toast.makeText(mContext, throwable.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun apiJsonToken(strRefreshToken: String?): JsonObject? {
        var gsonOtpResult = JsonObject()
        try {
            val paramAbTestResult = JSONObject()
            paramAbTestResult.put("refreshToken", strRefreshToken)
            val jsonParser = JsonParser()
            gsonOtpResult = jsonParser.parse(paramAbTestResult.toString()) as JsonObject
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return gsonOtpResult
    }
}