package com.greenusys.vehicledealerapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.*
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.greenusys.vehicledealerapp.activities.DashboardActivity
import com.greenusys.vehicledealerapp.activities.LoginActivity
import com.greenusys.vehicledealerapp.utilities.UserSession

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var mContext: Context
    private var splash_Screen_Time = 2000
    private val request_Code = 124
    private lateinit var userSession: UserSession

    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        mContext = this
        userSession = UserSession(mContext)

        getSoftwareVersion()

        checkMultiplePermissions()

    }

    private fun checkMultiplePermissions() {
        val permissionsNeeded: MutableList<String> = ArrayList()
        val permissionsList: MutableList<String> = ArrayList()
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Read Storage")
        }

        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Write Storage")
        }

        if (!addPermission(permissionsList, Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Manage Storage")
        }

        if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
            permissionsNeeded.add("Camera")
        }

        if (permissionsList.size > 0) {
            requestPermissions(
                permissionsList.toTypedArray<String>(), request_Code
            )
            return
        } else {
            getGotoNextPage()
        }
    }

    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission)
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission)) return false
        }
        return true
    }

    @RequiresApi(VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            request_Code -> {
                val perms: MutableMap<String, Int> = HashMap()
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.MANAGE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED

                // Fill with results
                var i = 0
                while (i < permissions.size) {
                    perms[permissions[i]] = grantResults[i]
                    i++
                }

                if (perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.MANAGE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                ) {
                    getGotoNextPage()
                    return
                } else {
                    getGotoNextPage()
//                    Toast.makeText(
//                        mContext, """
//                        My App cannot run without Location and Storage Permissions.
//                        Relaunch My App or allow permissions in Applications Settings
//                        """.trimIndent(), Toast.LENGTH_LONG
//                    ).show()
//                    finish()
                }
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getGotoNextPage() {
        Handler().postDelayed({
            try {
                if (userSession.isLoggedIn()) {
                    startActivity(
                        Intent(
                            mContext, DashboardActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    this@SplashScreenActivity.finish()
                } else {
                    startActivity(
                        Intent(
                            mContext, LoginActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    this@SplashScreenActivity.finish()
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }, splash_Screen_Time.toLong())
    }

    private fun getSoftwareVersion() {
        try {
            val pInfo = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
            val version = pInfo.versionName
            println("Software Version :- $version")
            userSession.setSoftwareVersion(version)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}