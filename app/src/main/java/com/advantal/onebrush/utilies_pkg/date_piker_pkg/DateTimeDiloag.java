package com.advantal.onebrush.utilies_pkg.date_piker_pkg;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.advantal.onebrush.utilies_pkg.AppConstant;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeDiloag extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private final DateTimeCallBack dateTimeCallBack;
    private Boolean disableForDOB = false;

    public DateTimeDiloag(DateTimeCallBack dateTimeCallBack, Boolean disableForDOB) {
        this.dateTimeCallBack = dateTimeCallBack;
        this.disableForDOB = disableForDOB;
        /***@StartRecrCheck use for Prevoius Date Disable for Start Date Filed*******/
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();


        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.MONTH, 00);
        calendar.set(Calendar.DAY_OF_MONTH, 01);


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        if (disableForDOB)
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        return datePickerDialog;
    }


    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String currentDateString = "";
        try {
            DateFormat formatter = new SimpleDateFormat(AppConstant.DATE_Frmt, Locale.getDefault());
            DecimalFormat formatte = new DecimalFormat("00");
            String str = String.valueOf(month + 1);
            String ss = (formatte.format(Integer.parseInt(str)));

            Date startDate = formatter.parse(year + "-" + ss + "-" + dayOfMonth);
            Calendar c = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//
//            String strDate = sdf.format(c.getTime());

            currentDateString = (new SimpleDateFormat(AppConstant.DATE_Frmt, Locale.getDefault()).format(startDate));//+" "+strDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateTimeCallBack.getDateTimeFromPicker(currentDateString);
    }


}
