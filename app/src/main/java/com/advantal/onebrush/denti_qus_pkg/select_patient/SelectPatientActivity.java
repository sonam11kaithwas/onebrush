package com.advantal.onebrush.denti_qus_pkg.select_patient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.advantal.onebrush.R;
import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.databinding.ActivitySelectPatientBinding;
import com.advantal.onebrush.denti_qus_pkg.select_patient.select_patient_model.PatientModel;
import com.advantal.onebrush.dentinostic_db.AppDataBase;
import com.advantal.onebrush.dentis_dash__board_pkg.bio_metric_pkg.FingerPrintCnfrmActivity;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.patient_list_pkg.PatientsViewModel;
import com.advantal.onebrush.utilies_pkg.AppConstant;
import com.advantal.onebrush.utilies_pkg.AppUtility;
import com.advantal.onebrush.utilies_pkg.MyCountDownTimer;
import com.advantal.onebrush.utilies_pkg.OneBrushApp;
import com.advantal.onebrush.utilies_pkg.one_brush_appprefrence_pkg.OneBrushAppPrefrence;

public class SelectPatientActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private UserAccountDetailsModel userAccountDetailsModel;
    private PatientAdapter patientAdapter;
    private ActivitySelectPatientBinding binding;
    private PatientsViewModel patientsViewModel;
    private MyCountDownTimer countDownTimer;
    private int counterForBio = 0;

    //    @Override
//    public void onUserInteraction() {
//        super.onUserInteraction();
//        Log.e("Test", " ---user interaction...");
//
//        //Reset the timer on user interaction...
//        AppUtility.timeCounterReset(countDownTimer);
//    }
//
//    private void userIntreactionEvent() {
//        if (userAccountDetailsModel.getIdMethod() != null &&
//                (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN) ||
//                        userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio))) {
////                MyDigitalClockForScreenLock.getInstance().stopTimerCounting();
////                MyDigitalClockForScreenLock.getInstance().setMyTimerTime(0);
//            if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_Bio)){
//                startActivity(new Intent(this, FingerPrintCnfrmActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                this.finish();}
//            else if (userAccountDetailsModel.getIdMethod().equals(AppConstant.ID_MENTHOD_PIN)) {
//                dashboardBinding.txtPinEntry.requestFocus();
//                dashboardBinding.pinViewLayout.setVisibility(View.VISIBLE);
//                dashboardBinding.layoutForView.setVisibility(View.GONE);
//            }
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_patient);
        recyclerView = findViewById(R.id.recycler_view_patient);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);// set status text dark
        patientsViewModel = ViewModelProviders.of(this)
                .get(PatientsViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_patient);
        binding.setLifecycleOwner(this);


        userAccountDetailsModel = AppDataBase.getInMemoryDatabase(OneBrushApp.getInstance()).userAccountDetailsDao().getAll(OneBrushAppPrefrence.getSharedprefInstance().getSelectedUserId());
        AppUtility.updateScreeState("SelectPatientActivity", userAccountDetailsModel);
//        countDownTimer = new MyCountDownTimer(AppConstant.startTime, AppConstant.interval, new MyCountDownTimer.UserIntractionDisable() {
//            @Override
//            public void getUserPinWindow() {
//                userIntreactionEvent();
//            }
//        });
//        AppUtility.timeCounterReset(countDownTimer);


        patientsViewModel.getNewPatients(new PatientModel(userAccountDetailsModel.getUuId()));
        //    patientsViewModel.getNewPatients(new PatientModel(userAccountDetailsModel.getUuId()));
//        patientsViewModel.getChangesgetpatient().observeForever(new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//
//
//            }
//        });
//        addPatientViewModel.getAddPatientSuccessFully().observeForever(new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//
//            }
//        });


//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        patientAdapter = new PatientAdapter(contactsList, this);
//        recyclerView.setAdapter(patientAdapter);


//        patientAdapter.notifyDataSetChanged();


    }
}