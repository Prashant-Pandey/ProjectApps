package com.mobility.inclass07.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.mobility.inclass07.R;
import com.mobility.inclass07.fragment.StepFragment;
import com.mobility.inclass07.utilities.AppConstant;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class StepperAdapter extends AbstractFragmentStepAdapter {

    String[] questions;

    public StepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
        questions = context.getResources().getStringArray(R.array.surveyQuestions);
    }

    @Override
    public Step createStep(int position) {
        final StepFragment step = new StepFragment();
        Bundle b = new Bundle();
        b.putString(AppConstant.SURVEY_QUESTION, questions[position]);
        step.setArguments(b);
        return step;
    }

    @Override
    public int getCount() {
        return AppConstant.QUEST_BANK_LENGTH;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return new StepViewModel.Builder(context)
                // .setTitle(R.string.tab_title) //can be a CharSequence instead
                .create();
    }
}
