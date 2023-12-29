package com.greenusys.vehicledealerapp.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.chaos.view.PinView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.greenusys.vehicledealerapp.R
import com.greenusys.vehicledealerapp.contracts.OtpContract
import com.greenusys.vehicledealerapp.contracts.ResendOtpContract
import com.greenusys.vehicledealerapp.models.OtpResponse
import com.greenusys.vehicledealerapp.models.ResendOtpResponse
import com.greenusys.vehicledealerapp.presenter.OtpPresenter
import com.greenusys.vehicledealerapp.presenter.ResendOtpPresenter
import com.greenusys.vehicledealerapp.utilities.UserSession
import org.json.JSONException
import org.json.JSONObject

class VerificationActivity : AppCompatActivity(), OtpContract.View, ResendOtpContract.View,
    View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var userSession: UserSession
    private lateinit var et_OtpCode: PinView
    private lateinit var resendOtp: TextView
    private lateinit var timeText: TextView
    private lateinit var otpVerify_btn: AppCompatButton
    private lateinit var otpPresenter: OtpPresenter
    private lateinit var resendOtpPresenter: ResendOtpPresenter
    private var strId: String = ""
    private var strPinCode: String? = ""
    private var strMailId: String? = ""
    private var strName: String? = ""
    private var strMobileNo: String? = ""
    private var strEmail: String? = ""
    private var strCity: String? = ""
    private var strTokenKey: String? = ""
    private var strRefreshTokenKey: String? = ""
    private lateinit var dialog: Dialog
    private lateinit var cTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        mContext = this
        userSession = UserSession(mContext)
        getLayoutUiIdFind()

        otpPresenter = OtpPresenter(this)
        resendOtpPresenter = ResendOtpPresenter(this)

        resendOtp.setOnClickListener(this)
        otpVerify_btn.setOnClickListener(this)

        startTimerForResendOtp()

    }

    private fun getLayoutUiIdFind() {
        try {
            et_OtpCode = findViewById<PinView>(R.id.et_OtpCode) as PinView
            resendOtp = findViewById<TextView>(R.id.resendOtp) as TextView
            timeText = findViewById<TextView>(R.id.timeText) as TextView
            otpVerify_btn = findViewById<AppCompatButton>(R.id.otpVerify_btn) as AppCompatButton
        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.otpVerify_btn -> {
                try {
                    if (isOtpDataValidate()) {
                        strId = userSession.getUserId().toString()
                        if (!strPinCode.equals("")) {
                            otpPresenter.requestOtpDataFromServer(
                                apiJsonSignOtp(strId, strPinCode.toString())
                            )
                        } else {
                            Toast.makeText(
                                mContext,
                                "Your entered OTP is going wrong! So, please enter valid OTP.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (exp: Exception) {
                    exp.stackTrace
                }
            }

            R.id.resendOtp -> {
                strId = userSession.getUserId().toString()
                resendOtpPresenter.requestResendOtpDataFromServer(strId)
            }
        }
    }

    private fun isOtpDataValidate(): Boolean {
        try {
            strPinCode = et_OtpCode?.text.toString().trim { it <= ' ' }
            if (strPinCode!!.isEmpty()) {
                et_OtpCode?.error = "Enter OTP"
                et_OtpCode?.requestFocus()
                return false
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
        return true
    }

    private fun apiJsonSignOtp(strId: String?, strOtp: String): JsonObject? {
        var gsonOtpResult = JsonObject()
        try {
            val paramAbTestResult = JSONObject()
            paramAbTestResult.put("verificationCode", strOtp)
            paramAbTestResult.put("_id", strId)
            val jsonParser = JsonParser()
            gsonOtpResult = jsonParser.parse(paramAbTestResult.toString()) as JsonObject
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return gsonOtpResult
    }

    override fun getResendOtpData(resendOtpResponse: ResendOtpResponse?) {
        if (resendOtpResponse?.success.equals("otp send success")) {
            Toast.makeText(
                mContext, "OTP resend successfully, kindly check your mail!", Toast.LENGTH_SHORT
            ).show()
            startTimerForResendOtp()
        } else {
            Toast.makeText(
                mContext,
                "Something went going wrong so, please retry after some time!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getLoginOtpData(otpResponse: OtpResponse?) {
        try {
            if (otpResponse?.newuser?.token != null || otpResponse?.newuser?.isVerified == true) {
                strId = otpResponse.newuser?.Id.toString()
                strName = otpResponse.newuser?.name.toString()
                strMobileNo = otpResponse.newuser?.mobile.toString()
                strEmail = otpResponse.newuser?.email.toString()
                strCity = otpResponse.newuser?.city.toString()
                strTokenKey = otpResponse.newuser?.token.toString()
                strRefreshTokenKey = otpResponse.newuser?.refreshToken.toString()
                userSession.createLoginSession(strMailId)
                userSession.setUserId(strId)
                userSession.setUserName(strName)
                userSession.setUserMobile(strMobileNo)
                println("Mail-Id :- $strEmail")
                userSession.setUserEmailId(strEmail)
                userSession.setUserCity(strCity)
                userSession.setServiceKeyToken(otpResponse.newuser?.token)
                userSession.setRefreshToken(otpResponse.newuser?.refreshToken)
                userSession.setOtpCode("")
                val intent = Intent(mContext, DashboardActivity::class.java)
                intent.putExtra("tokenKey", strTokenKey)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this@VerificationActivity.finish()
            } else {
                userSession.setOtpCode("")
                startActivity(
                    Intent(
                        mContext, ProfileFormDealerActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                this@VerificationActivity.finish()
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    override fun onResponseFailure(throwable: Throwable?) {
        Toast.makeText(mContext, throwable.toString(), Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        otpPresenter.onDestroy()
        resendOtpPresenter.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    private fun startTimerForResendOtp() {
        resendOtp.isEnabled = false

        cTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resendOtp.text = (millisUntilFinished / 1000).toString()
                timeText.text = "Didn't received a code? "
                resendOtp.isEnabled = false

            }

            override fun onFinish() {
                timeText.text = "Didn't received a code? "
                resendOtp.text = "Resend OTP"
                resendOtp.isEnabled = true
            }
        }
        cTimer.start()
    }
}