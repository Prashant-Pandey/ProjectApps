package com.mobility.inclass07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobility.inclass07.adapter.StepperAdapter;
import com.mobility.inclass07.fragment.StepFragment;
import com.mobility.inclass07.utilities.AppConstant;
import com.mobility.inclass07.utilities.Auth;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.HashMap;
import java.util.Map;

public class SurveyActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    private String teamId;
    private String teamName;
    private StepperLayout mStepperLayout;
    private StepperAdapter mStepperAdapter;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(AppConstant.TEAM_DB_KEY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           teamId =  extras.getString(AppConstant.TEAM_ID);
           teamName =  extras.getString(AppConstant.TEAM_NAME);
        }
        setTitle(teamName);

        mStepperLayout  = findViewById(R.id.stepperLayout);
        mStepperLayout.setListener(this);

        mStepperAdapter = new StepperAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(mStepperAdapter);
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
                finish();
            }
        });
    }

    @Override
    public void onCompleted(View completeButton) {
        submitForm();
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {}

    @Override
    public void onReturn() {
        Toast.makeText(this, "step return", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You need to finish the survey", Toast.LENGTH_SHORT).show();
    }
}
