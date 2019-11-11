package com.mobility.inclass07.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobility.inclass07.R;
import com.mobility.inclass07.utilities.AppConstant;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class StepFragment extends Fragment implements Step {

    String question;
    int data = AppConstant.RADIO_EMPTY_VAL, checkedRB = AppConstant.RADIO_EMPTY_VAL;
    RadioGroup radioGroup;

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(String param1, String param2) {
        StepFragment fragment = new StepFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            question = (String) getArguments().getSerializable(AppConstant.SURVEY_QUESTION);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView questionTV = view.findViewById(R.id.questionTV);
        questionTV.setText(question);

        radioGroup = view.findViewById(R.id.radioGroup);
        if (checkedRB != AppConstant.RADIO_EMPTY_VAL) {
            radioGroup.check(checkedRB);
        }
        onSelectedRadioBtn();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        VerificationError ve = new VerificationError(getActivity().getResources().getString(R.string.step_error));
        return data == AppConstant.RADIO_EMPTY_VAL ? ve : null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void onSelectedRadioBtn() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedRB = checkedId;
                switch(checkedId){
                    case R.id.radioButton1:
                        data = 1;
                        break;
                    case R.id.radioButton2:
                        data = 2;
                        break;
                    case R.id.radioButton3:
                        data = 3;
                        break;
                    case R.id.radioButton4:
                        data = 4;
                        break;
                    case R.id.radioButton5:
                        data = 5;
                        break;
                }
            }
        });

    }
}
