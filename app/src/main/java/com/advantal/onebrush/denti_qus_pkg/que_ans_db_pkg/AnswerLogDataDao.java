package com.advantal.onebrush.denti_qus_pkg.que_ans_db_pkg;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg.AnswerLogDataModel;

import java.util.List;

/**
 * Created by Sonam on 21-03-2022 12:15.
 */

@Dao
public interface AnswerLogDataDao {
    @Insert(onConflict = REPLACE)
    void insertAnswersData(AnswerLogDataModel answerLogDataModel);

    @Query("delete from answerLogDataModel")
    void delete();

    @Query("delete from answerLogDataModel where caseId=:caseId")
    void deleteAnsListByCaseId(int caseId);

    @Query("delete from answerLogDataModel where caseId=:caseId")
    void deleteAnswerByCaseId(int caseId);

    @Insert(onConflict = REPLACE)
    void insertAllAnswerSList(List<AnswerLogDataModel> answerLogDataModelList);

    @Update()
    void updateSongs(List<AnswerLogDataModel> answerLogDataModelList);

    @Query("select * from answerLogDataModel")
    List<AnswerLogDataModel> getAllQuesAnswerList();

    @Query("select * from answerLogDataModel where userId=:uuId")
    List<AnswerLogDataModel> getAllQuesAnswerListBySelectedUsr(String uuId);

    @Query("select * from answerLogDataModel where caseId=:caseId")
    List<AnswerLogDataModel> getAllQuesAnswerListByCaseId(int caseId);

    //    @Query("delete from answerLogDataModel where previousPosition=:crpos and caseId=:caseid")
    @Query("delete from answerLogDataModel where currentPosition=:crpos and caseId=:caseid")
    void deleteExitsAnswer(int crpos, int caseid);

    @Query("delete from answerLogDataModel where currentPosition=:crpos and caseId=:caseid")
    void deleteExitsAnswerModel(int crpos, int caseid);


    @Query("select * from answerLogDataModel where currentPosition=:crpos and caseId=:caseId")
    AnswerLogDataModel getExitAnswer(int crpos, int caseId);



    @Query("select * from answerLogDataModel where currentPosition=:currentPos and caseId=:caseid")
    AnswerLogDataModel getAnswerDataBy(int currentPos, int caseid);


    @Query("select * from answerLogDataModel where nextPosition=:nextPos and caseId=:caseid")
    AnswerLogDataModel getAnswer(int nextPos, int caseid);


    @Query("delete from answerLogDataModel where id >:id and caseId=:caseid")
    void removeByCur(int id, int caseid);

    @Update()
    void updateAnswer(AnswerLogDataModel answerLogDataModel);


    @Query("select * from answerLogDataModel where caseId=:caseIds")
    AnswerLogDataModel getcAnswerByCaseId(int caseIds);

    @Query("select * from answerLogDataModel where caseId=:caseIds")
    List<AnswerLogDataModel> getAnswerListByCaseId(int caseIds);


    @Query("select * from answerLogDataModel where caseId=:caseIds  and isType='Fileload'")
    AnswerLogDataModel getGalleryListByCaseId(int caseIds);


    @Query("UPDATE  answerLogDataModel SET ansSubmitOrnot = :ansSubmitOrnot  where caseId = :caseIds ")
    void updateSubmitAnswer(int caseIds, int ansSubmitOrnot);


    @Query("UPDATE  answerLogDataModel SET stateForWindow = :ansSubmitstate  where caseId = :caseIds ")
    void updateAnswerDataState(int caseIds, String ansSubmitstate);
}
