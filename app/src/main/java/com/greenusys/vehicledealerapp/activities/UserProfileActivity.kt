package com.greenusys.vehicledealerapp.activities

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.greenusys.vehicledealerapp.R
import com.greenusys.vehicledealerapp.contracts.ProfileContract
import com.greenusys.vehicledealerapp.models.ProfileResponse
import com.greenusys.vehicledealerapp.presenter.ProfilePresenter
import com.greenusys.vehicledealerapp.utilities.UserSession

class UserProfileActivity : AppCompatActivity(), ProfileContract.View, View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var userSession: UserSession
    private lateinit var backDashboardImg: ImageView
    private lateinit var dealerName_Tv: TextView
    private lateinit var dealerMobileNo_Tv: TextView
    private lateinit var dealerMailId_Tv: TextView
    private lateinit var dealerCity_Tv: TextView
    private lateinit var okayGotIt_btn: AppCompatButton
    private lateinit var profilePresenter: ProfilePresenter
    private lateinit var dialog: Dialog
    private var strUserId: String? = ""
    private var tokenKey: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        mContext = this
        userSession = UserSession(mContext)
        profilePresenter = ProfilePresenter(this)
        getLayoutUiIdFind()

        backDashboardImg.setOnClickListener(this)
        okayGotIt_btn.setOnClickListener(this)

        strUserId = userSession.getUserId()
        tokenKey = "Bearer " + userSession.getServiceKeyToken()

        println("User-Id & Token key :- $strUserId, $tokenKey")

        if (tokenKey != null) {
            profilePresenter.requestProfileDetailsDataFromServer(tokenKey, strUserId)
        } else {
            tokenKey = "Bearer " + userSession.getServiceKeyToken()
            Handler().postDelayed({
                try {
                    profilePresenter.requestProfileDetailsDataFromServer(tokenKey, strUserId)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }, 1000)
        }
    }

    private fun getLayoutUiIdFind() {
        try {
            backDashboardImg = findViewById<ImageView>(R.id.backDashboardImg) as ImageView
            dealerName_Tv = findViewById<TextView>(R.id.dealerName_Tv) as TextView
            dealerMobileNo_Tv = findViewById<TextView>(R.id.dealerMobileNo_Tv) as TextView
            dealerMailId_Tv = findViewById<TextView>(R.id.dealerMailId_Tv) as TextView
            dealerCity_Tv = findViewById<TextView>(R.id.dealerCity_Tv) as TextView
            okayGotIt_btn = findViewById<AppCompatButton>(R.id.okayGotIt_btn) as AppCompatButton

        } catch (exp: Exception) {
            exp.stackTrace
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backDashboardImg -> {
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.okayGotIt_btn -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
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

    override fun getProfileDetailsData(profileResponse: ProfileResponse?) {
        if (profileResponse?.user != null) {
            dealerName_Tv.text = profileResponse.user?.name
            dealerMobileNo_Tv.text = profileResponse.user?.mobile
            dealerMailId_Tv.text = profileResponse.user?.email
            dealerCity_Tv.text = profileResponse.user?.city

        } else {
            Toast.makeText(mContext, "Profile data not found!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResponseFailure(throwable: Throwable?) {
        Toast.makeText(mContext, throwable.toString(), Toast.LENGTH_SHORT).show()
    }
}