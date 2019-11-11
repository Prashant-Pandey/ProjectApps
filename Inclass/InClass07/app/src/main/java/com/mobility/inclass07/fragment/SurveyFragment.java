package com.mobility.inclass07.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobility.inclass07.R;
import com.mobility.inclass07.adapter.StepperAdapter;
import com.mobility.inclass07.listener.MainActionListener;
import com.mobility.inclass07.utilities.AppConstant;
import com.mobility.inclass07.utilities.Auth;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.HashMap;
import java.util.Map;

public class SurveyFragment extends Fragment implements StepperLayout.StepperListener {

    private String teamId;
    private String teamName;
    private StepperLayout mStepperLayout;
    private StepperAdapter mStepperAdapter;
    NavController navController;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(AppConstant.TEAM_DB_KEY);
    private MainActionListener mListener;

    public SurveyFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SurveyFragment newInstance(String param1, String param2) {
        SurveyFragment fragment = new SurveyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            teamId = (String) getArguments().getSerializable(AppConstant.TEAM_ID);
            teamName = (String) getArguments().getSerializable(AppConstant.TEAM_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mListener != null) {
            mListener.setTitle(teamName);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (MainActionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MainActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        mStepperLayout  = view.findViewById(R.id.stepperLayout);
        mStepperLayout.setListener(this);

        mStepperAdapter = new StepperAdapter(getActivity().getSupportFragmentManager(), getActivity());
        mStepperLayout.setAdapter(mStepperAdapter);
    }

    @Override
    public void onCompleted(View completeButton) {
        submitForm();
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(getActivity(), verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }


    private void submitForm() {
        double total = 0.0;
        for (int i = 0; i < AppConstant.QUEST_BANK_LENGTH; i++) {
            StepFragment sf = (StepFragment) mStepperAdapter.findStep(i);
            total += sf.data;
        }
        Double avg = total / AppConstant.QUEST_BANK_LENGTH;

        Map<String, Object> map = new HashMap<>();
        map.put(Auth.getCurrentUserID(), avg);
        mRootRef.child(teamId).child(AppConstant.RATINGS_DB_KEY).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                goToTeamList();
            }
        });
    }

    void goToTeamList(){
        navController.navigate(R.id.action_surveyFragment_to_teamListFragment);
    }
}
