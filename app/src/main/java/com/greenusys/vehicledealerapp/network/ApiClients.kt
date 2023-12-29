package com.greenusys.vehicledealerapp.network

import com.google.gson.GsonBuilder
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieHandler
import java.net.CookieManager
import java.util.concurrent.TimeUnit

object ApiClients {

    private const val BASE_URL = "http://testnode.shivshankartransport.xyz/user/"
//    private const val BASE_URL = "http://192.168.1.13:5000/user/"
//    private const val BASE_URL = "http://crm.shivshankartransport.xyz/user/"

    //        private const val BASE_URL = "https://wheelo.onrender.com/user/"
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {
        if (retrofit == null) {

            val interceptorLogin = HttpLoggingInterceptor()
            interceptorLogin.level = HttpLoggingInterceptor.Level.BODY

            val cookieHandlerLogin: CookieHandler = CookieManager()
            val clientLogin: OkHttpClient =
                OkHttpClient.Builder().addNetworkInterceptor(interceptorLogin)
                    .cookieJar(JavaNetCookieJar(cookieHandlerLogin))
                    .addInterceptor(interceptorLogin).retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build()

            val gson = GsonBuilder().setLenient().create()

            retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).client(clientLogin).build()
        }
        return retrofit
    }
}