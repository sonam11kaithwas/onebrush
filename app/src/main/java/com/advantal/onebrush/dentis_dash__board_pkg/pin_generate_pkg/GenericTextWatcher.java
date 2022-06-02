package com.advantal.onebrush.dentis_dash__board_pkg.pin_generate_pkg;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * Created by Surbhi on 2022-05-24 11:52 AM.
 */
public class GenericTextWatcher implements TextWatcher {

    private final View view;
    private final View nextView;
    PinVerification mListener;

    public GenericTextWatcher(View view, View nextView, PinVerification mListener) {
        this.view = view;
        this.nextView = nextView;
        this.mListener = mListener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();


//        switch (view.getId()) {
//            case R.id.editText1:
//            case R.id.editText2:
//            case R.id.editText3:
//            case R.id.editText4:
//                if (text.length() == 1)
//                    if (nextView != null) {
//                        nextView.requestFocus();
//                    } else {
//                        mListener.pinVerify(true);
//                    }
//
//                break;


    }
}

