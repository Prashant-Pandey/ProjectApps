package com.mobility.inclass07.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.admin.v1beta1.Progress;
import com.mobility.inclass07.R;

import java.util.ArrayList;


public class AuthFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    NavController navController;
    EditText emailET;
    EditText passwordET;
    TextView passwordTV;
    String email, password, TAG = this.getClass().getSimpleName();
    int CAMERA_REQUEST_CODE = 23;
    Activity activity;
    String[] cameraPermission = new String[]{Manifest.permission.CAMERA};
    SharedPreferences sharedPreferences;
    FirebaseAuth auth;
    FirebaseFirestore mDatabaseReference;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog progressDialog;

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
        FirebaseApp.initializeApp(activity);
        setHasOptionsMenu(false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        mDatabaseReference = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        assert activity != null;
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.login_email_shared_preference), Context.MODE_PRIVATE);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.login).setOnClickListener(this);
        passwordTV = view.findViewById(R.id.inputPasswordTextView);
        emailET = view.findViewById(R.id.email);
        passwordET = view.findViewById(R.id.password);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (getActivity().checkSelfPermission(cameraPermission[0]) != PackageManager.PERMISSION_GRANTED) {
//                activity.requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
//            }
//        }
        if (!sharedPreferences.getString(getString(R.string.login_email), "").equals("")) {
            if (getActivity().getIntent() != null) {
                FirebaseDynamicLinks.getInstance()
                        .getDynamicLink(getActivity().getIntent())
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                // Get deep link from result (may be null if no link is found)
                                Uri deepLink = null;
                                if (pendingDynamicLinkData != null) {
                                    deepLink = pendingDynamicLinkData.getLink();

                                    completeLogin(deepLink + "");
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
    }

    void completeLogin(String emailLink) {
        // Confirm the link is a sign-in with email link.
        Log.d(TAG, "onSuccess: " + emailLink);
        if (auth.isSignInWithEmailLink(emailLink)) {
            // Retrieve this from wherever you stored it
            email = sharedPreferences.getString(getString(R.string.login_email), "");
            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Successfully signed in with email link!");
                                goToNextActivity();
                            } else {
                                Log.e(TAG, "Error signing in with email link", task.getException());
                            }
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
    public void onClick(final View v) {
        email = emailET.getText().toString();

        switch (v.getId()) {
            case R.id.login: {
                // if the email exists in admin table
                progressDialog.show();
                mDatabaseReference.collection(getString(R.string.admin_collection)).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    ArrayList<String> adminArrayList = new ArrayList<>();
                                    assert task.getResult() != null;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, "onComplete: " + document.toString());
                                        adminArrayList.addAll(document.getData().keySet());

                                    }
                                    progressDialog.dismiss();
                                    if (adminArrayList.contains(email)) {
                                        sharedPreferences.edit().putString(getString(R.string.admin_login_email), email).apply();
                                        // go to admin page
                                        navController.navigate(R.id.action_authFragment_to_adminAuthFragment);
                                    } else {
                                        Log.d(TAG, "onComplete: not admin");
                                        loginWithLinkToEmail(email, v);
                                    }

//                                    if (!foundAdmin){
//                                        // login as a judge
//                                        loginWithLinkToEmail(email, v);
//                                    }
                                } else {
                                    progressDialog.dismiss();
                                    // login as a judge
                                    loginWithLinkToEmail(email, v);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // login as a judge
                                progressDialog.dismiss();
                                loginWithLinkToEmail(email, v);
                            }
                        });
                break;
            }
        }
    }


    private boolean loginWithLinkToEmail(final String email, final View v) {
        if (email == null || email.isEmpty()) {
            Toast.makeText(getContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        mDatabaseReference.collection("Event_Panelist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> panelistArrayList = new ArrayList<>();
                    assert task.getResult() != null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "onComplete: " + document.toString());
                        panelistArrayList.addAll(document.getData().keySet());
                    }
                    progressDialog.dismiss();
                    if (panelistArrayList.contains(email)) {
                        // send data to firebase
                        ActionCodeSettings actionCodeSettings =
                                ActionCodeSettings.newBuilder()
                                        .setUrl("https://schoolvote.page.link").setHandleCodeInApp(true)
                                        .setAndroidPackageName("com.mobility.inclass07", true, "19").build();
                        if (!progressDialog.isShowing()) progressDialog.show();
                        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // email is sent
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "An email has been sent to this ID, Please check!", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "Email sent.");
                                            // TODO: set email in shared preference
                                            sharedPreferences.edit().putString(getString(R.string.login_email), email).apply();
                                            // disable all inputs
                                            // keyboard down
                                            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                            assert inputMethodManager != null;
                                            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                        }

                                        Log.d(TAG, "" + task.getException());
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), "Unauthorized!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(getContext(), "You've not granted permissions", Toast.LENGTH_LONG).show();
        }
    }

    void goToNextActivity() {
        navController.navigate(R.id.action_authFragment_to_teamListFragment);
    }

}
