package com.greenusys.vehicledealerapp.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.greenusys.vehicledealerapp.R
import com.greenusys.vehicledealerapp.contracts.UserRegisterContract
import com.greenusys.vehicledealerapp.models.UserRegisterResponse
import com.greenusys.vehicledealerapp.presenter.UserRegisterPresenter
import com.greenusys.vehicledealerapp.utilities.UserSession
import org.json.JSONException
import org.json.JSONObject

class ProfileFormDealerActivity : AppCompatActivity(), UserRegisterContract.View,
    View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var userSession: UserSession
    private lateinit var name_Et: EditText
    private lateinit var mailId_Et: EditText
    private lateinit var mobileNumber_Et: EditText
    private lateinit var location_Et: EditText
    private lateinit var submit_btn: AppCompatButton
    private var strId: String? = ""
    private var strName: String? = ""
    private var strMailId: String? = ""
    private var strMobileNumber: String? = ""
    private var strLocation: String? = ""
    private lateinit var dialog: Dialog
    private lateinit var userRegisterPresenter: UserRegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_form_dealer)
        mContext = this
        userSession = UserSession(mContext)

        getLayoutUiIdFind()

        userRegisterPresenter = UserRegisterPresenter(this)

        strMailId = userSession.getUserEmailId().toString()
        println("User mail :- $strMailId")
        mailId_Et.setText(strMailId)
        strId = userSession.getUserId()

        submit_btn.setOnClickListener(this)

    }

    private fun getLayoutUiIdFind() {
        try {
            name_Et = findViewById<EditText>(R.id.name_Et) as EditText
            mailId_Et = findViewById<EditText>(R.id.mailId_Et) as EditText
            mobileNumber_Et = findViewById<EditText>(R.id.mobileNumber_Et) as EditText
            location_Et = findViewById<EditText>(R.id.location_Et) as EditText
            submit_btn = findViewById<AppCompatButton>(R.id.submit_btn) as AppCompatButton

            mailId_Et.setText(strMailId, TextView.BufferType.EDITABLE)
//            mailId_Et.setText(strMailId, TextView.BufferType.EDITABLE)
            mailId_Et.isClickable = false
            mailId_Et.isEnabled = false

        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.submit_btn -> {
                try {
                    if (isFormDataValidate()) {
                        var strJsonData: String = apiJsonRegister(
                            strId.toString(),
                            strName.toString(),
                            strMobileNumber.toString(),
                            strLocation.toString()
                        ).toString()
                        println("Data json :- $strJsonData")

                        userRegisterPresenter.requestRegisterDataFromServer(
                            jsonObject = apiJsonRegister(
                                strId.toString(),
                                strName.toString(),
                                strMobileNumber.toString(),
                                strLocation.toString()
                            )
                        )
                        userSession.setUserName(strName.toString())
                    }
                } catch (exp: Exception) {
                    exp.stackTrace
                }
            }
        }
    }

    private fun isFormDataValidate(): Boolean {
        try {
            strName = name_Et.text.toString().trim { it <= ' ' }
            strMobileNumber = mobileNumber_Et.text.toString().trim { it <= ' ' }
            strLocation = location_Et.text.toString().trim { it <= ' ' }
            if (strName!!.isEmpty()) {
                name_Et.error = "Enter name"
                name_Et.requestFocus()
                return false
            } else if (strMobileNumber!!.isEmpty()) {
                mobileNumber_Et.error = "Enter mobile number"
                mobileNumber_Et.requestFocus()
                return false
            } else if (strLocation!!.isEmpty()) {
                location_Et.error = "Enter location"
                location_Et.requestFocus()
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

    override fun getRegisterData(userRegisterResponse: UserRegisterResponse?) {
        if (userRegisterResponse?.token == null) {
            if (userRegisterResponse?.error.equals("user already registered")) {
                dialog.cancel()
                dialog.dismiss()
                Toast.makeText(
                    mContext, "Your are already registered!", Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(
                        mContext, DashboardActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                this@ProfileFormDealerActivity.finish()
            }
        } else if (userRegisterResponse?.token != null) {
            dialog.cancel()
            dialog.dismiss()
            userSession.setServiceKeyToken(userRegisterResponse?.token)
            Toast.makeText(
                mContext, "Profile created successfully!", Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(mContext, DashboardActivity::class.java)
            intent.putExtra("tokenKey", userRegisterResponse?.token)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this@ProfileFormDealerActivity.finish()
        } else {
            dialog.cancel()
            dialog.dismiss()
            Toast.makeText(
                mContext,
                "Something going wrong so, please retry after sometime!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResponseFailure(throwable: Throwable?) {
        Toast.makeText(mContext, throwable.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun apiJsonRegister(
        strId: String?, strName: String?, strMobileNumber: String?, strLocation: String?
    ): JsonObject? {
        var gsonMailResult = JsonObject()
        try {
            val paramAbTestResult = JSONObject()
            paramAbTestResult.put("_id", strId)
            paramAbTestResult.put("name", strName)
            paramAbTestResult.put("mobile", strMobileNumber)
            paramAbTestResult.put("city", strLocation)

            val jsonParser = JsonParser()
            gsonMailResult = jsonParser.parse(paramAbTestResult.toString()) as JsonObject
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return gsonMailResult
    }
}