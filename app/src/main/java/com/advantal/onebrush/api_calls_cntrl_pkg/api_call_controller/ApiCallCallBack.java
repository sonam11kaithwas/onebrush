package com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller;


import com.google.gson.JsonObject;

public interface ApiCallCallBack {
    /************server request code for multiple calls**************/
    int GetWLCrosl = 1;
    int UserReg = 2;
    int UserLogin = 3;
    int IsEmailExits = 4;
    int IsEmailForgot = 5;
    int Logout = 6;
    int FeedBack = 7;
    int ChangePassword = 8;
    int ProfileUpdate = 9;
    int AddNewPatient = 10;
    int GetNewPatient = 11;
    int GetAllQuestions = 12;
    int GetSyncronization = 13;
    int GetAllCases = 14;
    int isUserVerified = 15;
    int GetMultiQuestions = 16;
    int GetSCreenDetails = 18;
    int GetCountry = 19;
    int isEmailValid= 20;


    void getSuccessResponce(JsonObject jsonObject, int requestCode);

    void getApiErrorResponce(String error,String titleHeader);

    void getApionComplete();
}
