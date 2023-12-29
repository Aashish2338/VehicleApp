package com.greenusys.vehicledealerapp.network

import com.google.gson.JsonObject
import com.greenusys.vehicledealerapp.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("signin")
    fun getLoginData(@Body jsonObject: JsonObject?): Call<SigningResponse?>?

    @POST("verify")
    fun getVerifyOtpData(@Body jsonObject: JsonObject?): Call<OtpResponse?>?

    @GET("resendotp/{id}")
    fun getResendOtpData(@Path("id") strId: String?): Call<ResendOtpResponse?>?

    @POST("register")
    fun getUserRegisterData(@Body jsonObject: JsonObject?): Call<UserRegisterResponse?>?

    @GET("getVehicles")
    fun getAllVehicleData(@Header("Authorization") strToken: String?): Call<VehicleListResponse?>?

    @GET("getVehicle")
    fun getVehicleDetailsData(
        @Header("Authorization") strToken: String?, @Query("id") strVehicleId: String?
    ): Call<VehicleDetailsResponse?>?

    @GET("profile")
    fun getUserProfileData(
        @Header("Authorization") strToken: String?, @Query("id") strVehicleId: String?
    ): Call<ProfileResponse?>?

    @DELETE("deleteVehicle/{id}")
    fun getDeleteVehicleData(
        @Header("Authorization") strToken: String?, @Path("id") strId: String?
    ): Call<ResendOtpResponse?>?

    @Multipart
    @POST("addVehicle")
    fun getAddVehicleA(
        @Header("Authorization") strToken: String?,
        @Part("vehicleNumber") vehicleNumber: RequestBody?,
        @Part("hypotheticationRC") hypotheticalRC: RequestBody?,
        @Part("insuranceValid") insurance: RequestBody?,
        @Part("otherDocument") otherDocs: RequestBody?,
        @Part rcImage: MultipartBody.Part?,
        @Part BankNOCImage: MultipartBody.Part?,
        @Part insuranceImage: MultipartBody.Part?,
        @Part otherDocumentImage: MultipartBody.Part?
    ): Call<VehicleResponse?>?

    @Multipart
    @POST("addVehicle")
    fun getAddVehicleB(
        @Header("Authorization") strToken: String?,
        @Part("vehicleNumber") vehicleNumber: RequestBody?,
        @Part("hypotheticationRC") hypotheticalRC: RequestBody?,
        @Part("insuranceValid") insurance: RequestBody?,
        @Part("otherDocument") otherDocs: RequestBody?,
        @Part rcImage: MultipartBody.Part?,
        @Part BankNOCImage: MultipartBody.Part?,
        @Part insuranceImage: MultipartBody.Part?
    ): Call<VehicleResponse?>?

    @Multipart
    @POST("addVehicle")
    fun getAddVehicleC(
        @Header("Authorization") strToken: String?,
        @Part("vehicleNumber") vehicleNumber: RequestBody?,
        @Part("hypotheticationRC") hypotheticalRC: RequestBody?,
        @Part("insuranceValid") insurance: RequestBody?,
        @Part("otherDocument") otherDocs: RequestBody?,
        @Part rcImage: MultipartBody.Part?,
        @Part BankNOCImage: MultipartBody.Part?
    ): Call<VehicleResponse?>?

    @Multipart
    @POST("addVehicle")
    fun getAddVehicleD(
        @Header("Authorization") strToken: String?,
        @Part("vehicleNumber") vehicleNumber: RequestBody?,
        @Part("hypotheticationRC") hypotheticalRC: RequestBody?,
        @Part("insuranceValid") insurance: RequestBody?,
        @Part("otherDocument") otherDocs: RequestBody?,
        @Part rcImage: MultipartBody.Part?,
        @Part insuranceImage: MultipartBody.Part?
    ): Call<VehicleResponse?>?

    @Multipart
    @POST("addVehicle")
    fun getAddVehicleE(
        @Header("Authorization") strToken: String?,
        @Part("vehicleNumber") vehicleNumber: RequestBody?,
        @Part("hypotheticationRC") hypotheticalRC: RequestBody?,
        @Part("insuranceValid") insurance: RequestBody?,
        @Part("otherDocument") otherDocs: RequestBody?,
        @Part rcImage: MultipartBody.Part?,
        @Part otherDocumentImage: MultipartBody.Part?
    ): Call<VehicleResponse?>?

    @Multipart
    @POST("addVehicle")
    fun getAddVehicle(
        @Header("Authorization") strToken: String?,
        @Part("vehicleNumber") vehicleNumber: RequestBody?,
        @Part("hypotheticationRC") hypotheticalRC: RequestBody?,
        @Part("insuranceValid") insurance: RequestBody?,
        @Part("otherDocument") otherDocs: RequestBody?,
        @Part rcImage: MultipartBody.Part?
    ): Call<VehicleResponse?>?

    @POST("refreshtoken")
    fun getRefreshToken(
        @Body jsonObject: JsonObject?
    ): Call<RefreshTokenResponse?>?
}