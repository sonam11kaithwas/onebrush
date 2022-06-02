package com.advantal.onebrush.api_calls_cntrl_pkg;

import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Sonam on 17-02-2022 12:28.
 */

public interface ApiServices {
    String get_welcome_carousel = "admin/get_welcome_carousel";
    /*********this API for android because old not support Json formate*********/
    String customer_user_register = "customer/user_register";//customer/user_registers
    String user_register = "customer/isVerified";
    String user_Login = "customer/user_login";
    String isUser_account_exist = "customer/isUser_account_exist";
    String isEmailValid = "customer/isEmailValid";
    //    String forgot_password = "customer/forgot_password";
    String forgot_password = "customer/forgot_password_new";
    String user_logout = "customer/logout";
    String feed_back = "customer/user_feedback";
    String change_password = "customer/user_change_password";
    String user_profile_update = "customer/user_profile_update";
    String save_Patient = "patient/save_Patient";
    String get_Patient = "customer/get_user_profile";
    String getAllQuestions = "question/getAllQuestions";
    //    String add_case = "cases/add_case";
    String isUserVerified = "customer/isUserVerified";
    String get_all_cases_by_uuid = "cases/get_all_cases_by_uuid";//
    String get_all_syncronization = "syncronization/get_all_syncronization";
    String get_all_multicontrol = "admin/get_all_multicontrol";
    String get_content = "admin/get_content";
    //    String get_content = "admin/ge̥t_content";
    String get_all_screen_Detail = "admin/get_all_screen_Detail";
    String get_country = "admin/get_all_country";


    @GET("admin/get_all_screen_Detail")
    Observable<JsonObject> getWlcCarousle();

    @GET
    Observable<JsonObject> getWlcCarousle(@Url String url);

    @POST
    Observable<JsonObject> apiCall(@Url String url, @Body JsonObject request);

    @POST
    Observable<JsonObject> getDataFromServerWithParameters(@Url String url);


    @GET
    Observable<JsonObject> getPatientList(@Url String fileUrl);

    @GET("cases/get_all_cases_by_uuid")
    Observable<JsonObject> getCaseDataList(@Query("uuId") String uuId);


    @Multipart
    @POST("cases/add_case")
    Observable<JsonObject> addCases(
//    Call<JsonObject> addCases(

            @Part MultipartBody.Part[] xRayImages,
//            @Part List<MultipartBody.Part> xRayImages,
            @Part("uuId") RequestBody uuId, @Part("status") RequestBody status,
            @Part("usrId") RequestBody usrId
            , @Part("forProfile") RequestBody forProfile
            , @Part("dentist") RequestBody dentist, @Part("perscriptionID") RequestBody perscriptionID
            , @Part("paymentSystem") RequestBody paymentSystem,
            @Part("paymentEvidence") RequestBody paymentEvidence,
            @Part("logRecordsListStr") RequestBody logRecordsListStr,
            @Part("caseId") RequestBody caseId,
            @Part("recordCreationFunction") RequestBody recordCreationFunction
            ,@Part("recordCreationProfile") RequestBody recordCreationProfile
    );


    @Multipart
    @POST("admin/error_log_file_upload")
    Observable<JsonObject> sendLogReport(
            @Part MultipartBody.Part docfile,
            @Part("custId") RequestBody custId, @Part("recordCreationFunction") RequestBody recordCreationFunction,
            @Part("recordCreationProfile") RequestBody recordCreationProfile, @Part("languageCode") RequestBody languageCode
    );

    @FormUrlEncoded
    @POST(isUserVerified)
    Observable<JsonObject> isUserVerified(@Field("languageCode") String languageCode, @Field("uuId") String uuId);

    @PUT()
    Observable<JsonObject> updateUser(@Url String url, @Body JsonObject body);
}
