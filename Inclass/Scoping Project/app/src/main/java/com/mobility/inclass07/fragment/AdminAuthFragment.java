package com.mobility.inclass07.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobility.inclass07.R;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminAuthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminAuthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAuthFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Activity activity;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    NavController navController;
    String email, password, TAG = this.getClass().getSimpleName();
    EditText passwordET;
    ProgressDialog progressDialog;

    public AdminAuthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminAuthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAuthFragment newInstance(String param1, String param2) {
        AdminAuthFragment fragment = new AdminAuthFragment();
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
        email = sharedPreferences.getString(getString(R.string.admin_login_email), "");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.login).setOnClickListener(this);
        passwordET = view.findViewById(R.id.password);
    }

    private boolean loginWithEmailAndPassword(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            Toast.makeText(getContext(), R.string.invalid_email_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    goToAdminFragment();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Password Incorrect! Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;
    }

    void goToAdminFragment() {
        navController.navigate(R.id.action_adminAuthFragment_to_teamListFragment);
        Log.d(TAG, "goToAdminFragment: login successfull");
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
        switch (v.getId()) {
            case R.id.login: {
                Log.d(TAG, "onClick: login button");
                password = passwordET.getText().toString();
                if (email != null && !Objects.equals(password, "")) {
                    loginWithEmailAndPassword(email, password);
                    Log.d(TAG, "onClick: login password work");
                } else {
                    Toast.makeText(getContext(), "Please provide valid password", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
