package com.advantal.onebrush.dentis_dash__board_pkg.dentis_case_information.denti_case_pkg.denti_qus_pkg.invoice_pkg;

import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.ApiCallCallBack;
import com.google.gson.JsonObject;

/**
 * Created by Sonam on 25-04-2022 16:50.
 */
public class InvoiceViewModel extends ViewModel implements ApiCallCallBack {
    @Override
    public void getSuccessResponce(JsonObject jsonObject, int requestCode) {

    }

    @Override
     public void getApiErrorResponce(String error, String titleHeader) {

    }

    @Override
    public void getApionComplete() {

    }
}
