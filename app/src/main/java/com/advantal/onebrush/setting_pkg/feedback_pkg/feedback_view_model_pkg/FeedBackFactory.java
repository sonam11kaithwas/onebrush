package com.advantal.onebrush.setting_pkg.feedback_pkg.feedback_view_model_pkg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.advantal.onebrush.setting_pkg.feedback_pkg.feedback_model_pkg.FeedBackModel;

/**
 * Created by Surbhi joshi on 09-03-2022 11:48.
 */
public class FeedBackFactory extends ViewModelProvider.NewInstanceFactory {
    private final Context context;
    private final FeedBackModel feedBackModel;

    public FeedBackFactory(Context context, FeedBackModel feedBackModel) {
        this.context = context;
        this.feedBackModel = feedBackModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FeedBackViewModel(context, feedBackModel);

    }
}
