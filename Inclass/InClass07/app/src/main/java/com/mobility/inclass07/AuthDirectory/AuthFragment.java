package com.mobility.inclass07.AuthDirectory;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.mobility.inclass07.R;

import java.util.Objects;


public class AuthFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    NavController navController;
    EditText emailET;
    TextView loginMessage;
    String email, TAG = this.getClass().getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AuthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuthFragment newInstance(String param1, String param2) {
        AuthFragment fragment = new AuthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.login).setOnClickListener(this);
        emailET = view.findViewById(R.id.email);
        loginMessage = view.findViewById(R.id.loginMessage);

        if (getActivity().getIntent()!=null){
            FirebaseApp.initializeApp(Objects.requireNonNull(getActivity()));
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getActivity().getIntent())
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                                Log.d(TAG, "onSuccess: "+deepLink);
                                navController.navigate(R.id.action_authFragment_to_surveyForm);
                            }


                        }
                    })
                    .addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "getDynamicLink:onFailure", e);
                        }
                    });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:{
                email = emailET.getText().toString();
                if (email.isEmpty()||email==null){
                    Toast.makeText(getContext(), R.string.auth_failed, Toast.LENGTH_SHORT).show();
                }else{
                    // send data to firebase
                    ActionCodeSettings actionCodeSettings =
                            ActionCodeSettings.newBuilder()
                                    .setUrl("https://schoolvote.page.link").setHandleCodeInApp(true)
                                    .setAndroidPackageName("com.mobility.inclass07",true,"19").build();

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendSignInLinkToEmail(email, actionCodeSettings)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // email is sent
                                        Log.d(TAG, "Email sent.");
                                        // change the system state to processing
                                        loginMessage.setText(R.string.check_email_instruction);
                                        // disable all inputs
                                        // keyboard down
                                    }

                                    Log.d(TAG, ""+task.getException());
                                }
                            });
//                    navController.navigate(R.id.action_authFragment_to_surveyForm);
                }
                break;
            }
        }
    }

}
