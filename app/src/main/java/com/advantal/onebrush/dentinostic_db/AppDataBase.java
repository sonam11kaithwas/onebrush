package com.advantal.onebrush.dentinostic_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.advantal.onebrush.UserAccountDetailsModel;
import com.advantal.onebrush.denti_qus_pkg.que_ans_db_pkg.AnswerLogDataDao;
import com.advantal.onebrush.denti_qus_pkg.que_ans_db_pkg.MultiCntrlQuesDao;
import com.advantal.onebrush.denti_qus_pkg.que_ans_db_pkg.QuestionDao;
import com.advantal.onebrush.denti_qus_pkg.que_ans_db_pkg.XRayImageDao;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerImageCoverter;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerLogDataModel;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.MultiControlQueModel;
import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.QuestionModel;
import com.advantal.onebrush.dentinostic_db.user_account_details_pkg.UserAccountDetailsDao;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg.AddPatientModel;
import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.patient_db.PatientDao;
import com.advantal.onebrush.registration_pkg.add_case_pkg.XRayImageListModl;

/**
 * Created by Sonam on 21-02-2022 16:22.
 */
@Database(entities = {UserAccountDetailsModel.class, QuestionModel.class, MultiControlQueModel.class, AnswerLogDataModel.class
        , AddPatientModel.class, XRayImageListModl.class
},
        exportSchema = false, version = 1)
@TypeConverters({AnswerImageCoverter.class})

abstract public class AppDataBase extends RoomDatabase {
    private static final String DB_NAME = "onebrush_db";
    private static AppDataBase INSTANCE;

    public static AppDataBase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDataBase.class, DB_NAME)
                    // To simplify the codelab, allow queries on the main thread.
                    // Don't do this on a real app! See PersistenceBasicSample for an example.
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract UserAccountDetailsDao userAccountDetailsDao();

    public abstract MultiCntrlQuesDao multiCntrlQuesDao();

    public abstract QuestionDao questionDao();

    public abstract AnswerLogDataDao answerLogDataDao();

    public abstract PatientDao patientDao();

    public abstract XRayImageDao xRayImageDao();
}
