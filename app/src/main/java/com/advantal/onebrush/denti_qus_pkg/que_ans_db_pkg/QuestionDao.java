package com.advantal.onebrush.denti_qus_pkg.que_ans_db_pkg;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.QuestionModel;

import java.util.List;

/**
 * Created by Sonam on 08-03-2022 12:08.
 */

@Dao
public interface QuestionDao {
    @Query("select * from questionModel")
    QuestionModel getAll();

    @Query("delete from questionModel")
    void delete();

    @Insert(onConflict = REPLACE)
    void insertAllQuestions(List<QuestionModel> questionModelList);

    @Query("delete from questionModel")
    void deleteQuestionData();

    @Query("select * from questionModel")
    List<QuestionModel> getAllQuesList();

    @Query("select * from questionModel where position=:position")
    List<QuestionModel> getAllQuesListByPos(int position);
    @Query("select * from questionModel where position=:position")
 QuestionModel getAllQuesModelByCurrPos(int position);

    @Query("select * from questionModel where nextPosition=:position")
    List<QuestionModel> getAllQuesListByNxtPos(int position);

    @Query("select * from questionModel where nextPosition=:nxtpos")
    List<QuestionModel> getAllQuesListByMulti(int nxtpos);

    @Query("select * from questionModel where nextPosition=:position")
    QuestionModel getAllQuesListByNxtPoss(int position);

    @Query("select * from questionModel where position=:position")
    List<QuestionModel> getByCurrentPosition(int position);
}
