package com.greenusys.vehicledealerapp.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.greenusys.vehicledealerapp.R
import com.greenusys.vehicledealerapp.contracts.UserSigningContract
import com.greenusys.vehicledealerapp.models.SigningResponse
import com.greenusys.vehicledealerapp.presenter.SigningPresenter
import com.greenusys.vehicledealerapp.utilities.UserSession
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), UserSigningContract.View, View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var userSession: UserSession
    private var mailId_Et: EditText? = null
    private lateinit var submit_btn: AppCompatButton

    //    private lateinit var otpVerify_btn: AppCompatButton
//    private lateinit var getOtp_CardView: CardView
//    private lateinit var otpVerify_CardView: CardView
    private var strMailId: String? = ""
    private var strId: String? = ""
    private lateinit var dialog: Dialog
    private lateinit var signingPresenter: SigningPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mContext = this
        userSession = UserSession(mContext)

        getLayoutViewIdFind()

        submit_btn.setOnClickListener(this)
        signingPresenter = SigningPresenter(this)

    }

    private fun getLayoutViewIdFind() {
        try {
            mailId_Et = findViewById<EditText>(R.id.mailId_Et) as EditText
            submit_btn = findViewById<AppCompatButton>(R.id.submit_btn) as AppCompatButton
//            otpVerify_btn = findViewById<AppCompatButton>(R.id.otpVerify_btn) as AppCompatButton
//            getOtp_CardView = findViewById<CardView>(R.id.getOtp_CardView) as CardView
//            otpVerify_CardView = findViewById<CardView>(R.id.otpVerify_CardView) as CardView

        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.submit_btn -> {
                try {
                    if (isFormDataValidate()) {
                        userSession.setUserEmailId(strMailId.toString())
                        signingPresenter.requestDataFromServer(apiJsonSign(strMailId.toString()))
                    }
                } catch (exp: Exception) {
                    exp.stackTrace
                }
            }
        }
    }

    private fun isFormDataValidate(): Boolean {
        try {
            strMailId = mailId_Et?.text.toString().trim { it <= ' ' }
            if (strMailId!!.isEmpty()) {
                mailId_Et?.error = "Enter mail-Id"
                mailId_Et?.requestFocus()
                return false
            }
        } catch (exp: Exception) {
            exp.stackTrace
        }
        return true
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

    @SuppressLint("SetTextI18n")
    override fun getLoginUserData(signingResponse: SigningResponse?) {
        if (signingResponse?.success == "otp send success") {
            strId = signingResponse.id.toString()
            userSession.setUserId(strId)
            Toast.makeText(
                mContext,
                "OTP has been sent on your mail kindly, check your mail!",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(
                Intent(
                    mContext, VerificationActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            this@LoginActivity.finish()
        } else {
            Toast.makeText(
                mContext, "Something went wrong kindly, retry after some time!", Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResponseFailure(throwable: Throwable?) {
        Toast.makeText(mContext, throwable.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun apiJsonSign(emailId: String): JsonObject? {
        var gsonMailResult = JsonObject()
        try {
            val paramAbTestResult = JSONObject()
            paramAbTestResult.put("email", emailId)
            val jsonParser = JsonParser()
            gsonMailResult = jsonParser.parse(paramAbTestResult.toString()) as JsonObject
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return gsonMailResult
    }

    override fun onDestroy() {
        super.onDestroy()
        signingPresenter.onDestroy()
    }
}