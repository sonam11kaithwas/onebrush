package com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.patient_db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.advantal.onebrush.dentis_dash__board_pkg.dentinostic_pkg.patient_pkg.add_patient_pkg.AddPatientModel;

import java.util.List;

/**
 * Created by Sonam on 31-03-2022 16:42.
 */
@Dao
public interface PatientDao {


    @Insert(onConflict = REPLACE)
    void insertPatientModel(AddPatientModel addPatientModel);

    @Insert(onConflict = REPLACE)
    void insertMorePatientModel(List<AddPatientModel> addPatientModel);

    @Query("select * from addPatientModel")
    List<AddPatientModel> getAllPAtientList();

    @Query("select * from addPatientModel where uuId=:uuId")
    AddPatientModel getAllPatientById(String uuId);

    @Query("delete from addPatientModel")
    void delete();

    @Update
    void updatePatientData(AddPatientModel addPatientModel);

    @Delete
    void deletePatientData(AddPatientModel addPatientModel);
}
