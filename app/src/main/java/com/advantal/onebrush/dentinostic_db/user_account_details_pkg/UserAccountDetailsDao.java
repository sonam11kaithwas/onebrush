package com.advantal.onebrush.dentinostic_db.user_account_details_pkg;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.advantal.onebrush.UserAccountDetailsModel;

import java.util.List;

/**
 * Created by Sonam on 21-02-2022 16:27.
 */
@Dao
public interface UserAccountDetailsDao {

    @Insert(onConflict = REPLACE)
    void insertSinleUserAccountDetails(UserAccountDetailsModel userAccountDetailsModels);

    @Update
    void updateAccountDetails(UserAccountDetailsModel userAccountDetailsModels);

    @Query("delete from userAccountDetailsModel")
    void delete();

    @Query("select * from userAccountDetailsModel where uuId=:uuId")
    UserAccountDetailsModel getAll(String uuId);


    @Query("select * from userAccountDetailsModel")
    List<UserAccountDetailsModel> getAllUserList();

    @Query("select * from userAccountDetailsModel where idMethod=:idMethodBio")
    UserAccountDetailsModel getBioMetricData(String idMethodBio);

    @Query("select * from userAccountDetailsModel where registeredPIN1=:registeredPIN1 and  uuId != :uuId ")
    UserAccountDetailsModel getExitPinData(String registeredPIN1,String uuId);


    @Query("UPDATE userAccountDetailsModel SET uuId = :uuId WHERE uuId = 1")
    void updateUuId(String uuId);
}
