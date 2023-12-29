package com.greenusys.vehicledealerapp.utilities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.greenusys.vehicledealerapp.activities.LoginActivity

class UserSession(mContext: Context) {

    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var _context: Context? = null
    private val PRIVATE_MODE = 0
    private val PREF_NAME = "Greenusys"
    private val IS_LOGIN = "IsLoggedIn"
    private val KEY_EMAIL = "email"

    init {
        this._context = mContext
        pref = _context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref!!.edit()
    }

    fun createLoginSession(mailId: String?) {
        editor!!.putBoolean(IS_LOGIN, true)
        editor!!.putString(KEY_EMAIL, mailId)
        editor!!.commit()
    }

    fun setSoftwareVersion(softwareVersion: String?) {
        editor!!.putString("softwareVersion", softwareVersion)
        editor!!.commit()
    }

    fun getSoftwareVersion(): String? {
        return pref!!.getString("softwareVersion", "")
    }

    fun setUserId(userId: String?) {
        editor!!.putString("EmpCode", userId)
        editor!!.commit()
    }

    fun getUserId(): String? {
        return pref!!.getString("EmpCode", "")
    }

    fun setVehicleId(vehicleId: String?) {
        editor!!.putString("vehicleId", vehicleId)
        editor!!.commit()
    }

    fun getVehicleId(): String? {
        return pref!!.getString("vehicleId", "")
    }

    fun setUserName(userName: String?) {
        editor!!.putString("userName", userName)
        editor!!.commit()
    }

    fun getUserName(): String? {
        return pref!!.getString("userName", "")
    }

    fun setUserMobile(userMobile: String?) {
        editor!!.putString("userMobile", userMobile)
        editor!!.commit()
    }

    fun getUserMobile(): String? {
        return pref!!.getString("userMobile", "")
    }

    fun setUserEmailId(userEmailId: String?) {
        editor!!.putString("userEmailId", userEmailId)
        editor!!.commit()
    }

    fun getUserEmailId(): String? {
        return pref!!.getString("userEmailId", "")
    }

    fun setOtpCode(otpCode: String?) {
        editor!!.putString("otpCode", otpCode)
        editor!!.commit()
    }

    fun getOtpCode(): String? {
        return pref!!.getString("otpCode", "")
    }

    fun setUserCity(userCity: String?) {
        editor!!.putString("userCity", userCity)
        editor!!.commit()
    }

    fun getUserCity(): String? {
        return pref!!.getString("userCity", "")
    }

    fun setServiceKeyToken(serviceKeyToken: String?) {
        editor!!.putString("serviceKeyToken", serviceKeyToken)
        editor!!.commit()
    }

    fun getServiceKeyToken(): String? {
        return pref!!.getString("serviceKeyToken", "")
    }

    fun setRefreshToken(refreshToken: String?) {
        editor!!.putString("refreshToken", refreshToken)
        editor!!.commit()
    }

    fun getRefreshToken(): String? {
        return pref!!.getString("refreshToken", "")
    }

    fun logoutUser() {
        editor!!.clear()
        editor!!.commit()
        val i = Intent(_context, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        _context!!.startActivity(i)
    }

    fun isLoggedIn(): Boolean {
        return pref!!.getBoolean(IS_LOGIN, false)
    }
}