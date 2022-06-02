package com.advantal.onebrush.denti_qus_pkg.que_ans_db_pkg;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.MultiControlQueModel;

import java.util.List;

/**
 * Created by Sonam on 08-03-2022 12:09.
 */

@Dao
public interface MultiCntrlQuesDao {

    @Query("select * from multiControlQueModel")
    List<MultiControlQueModel> getAllMultiQuestionDataList();


    @Query("delete from multiControlQueModel")
    void delete();

    @Insert(onConflict = REPLACE)
    void insertAllQuestions(List<MultiControlQueModel> questionModelList);

//    @Query("select * from multiControlQueModel where nextPosition=:MCkey")
//    List<MultiControlQueModel> getAllQuesListByMckey(int MCkey);
//
    @Query("select * from multiControlQueModel where multiControlKey=:multiControlKey and resultingCode = :res")
    MultiControlQueModel getposiByMultiKey(int multiControlKey,String res);

    @Query("select * from multiControlQueModel where multiControlKey=:MCkey and resultingCode = :resultingCode")
    MultiControlQueModel getAllMulQuesListByResultingCode(int MCkey, String resultingCode);

    @Query("delete from multiControlQueModel")
    void deletMultiQuesData();
}
