package com.advantal.onebrush.denti_qus_pkg.que_ans_db_pkg;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.advantal.onebrush.registration_pkg.add_case_pkg.XRayImageListModl;

import java.util.List;

/**
 * Created by Sonam on 11-05-2022 17:14.
 */

@Dao
public interface XRayImageDao {

    @Query("select * from xRayImageListModl")
    List<XRayImageListModl> getAllXRaysList();

    @Query("select * from xRayImageListModl where caseId=:caseId")
    List<XRayImageListModl> getAllXRaysListByCaseId(int caseId);

    @Insert(onConflict = REPLACE)
    void insertAllXRaysList(List<XRayImageListModl> xRayImageListModlList);

//    @Query("delete from xRayImageListModl")
//    void delete();
}
