package com.advantal.onebrush.denti_qus_pkg.que_ans_mode_pkg;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sonam on 30-03-2022 12:56.
 */
public class AnswerImageCoverter {
    @TypeConverter
    public static List<String> toAnswerImgData(String strdata) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringAnswerImgData(List<String> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
