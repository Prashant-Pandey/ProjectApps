package com.mobility.inclass04;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobility.inclass04.Utils.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class StartUpFragment extends Fragment implements View.OnClickListener {

    NavController navController;
    SharedPreferences sharedPref;
    String TAG = "demo";

    public StartUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (sharedPref.getString(getString(R.string.userToken), "").equals("")) {
            navController.navigate(R.id.action_startUpFragment_to_logInFragment);
        } else {
            new validateTokenAsync().execute();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_up, container, false);
    }

    @Override
    public void onClick(View view) {

    }


    private class validateTokenAsync extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog progressDialog;
        User user;

        public validateTokenAsync() {
            this.user = new User();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (bool == false) {
                navController.navigate(R.id.action_startUpFragment_to_logInFragment);
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userDetails", this.user);
                navController.navigate(R.id.action_startUpFragment_to_productListFragment, bundle);

            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(Void... voids) {

            sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            final OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .header("Authorization", "Bearer " + sharedPref.getString(getString(R.string.userToken), ""))
                    .url(getString(R.string.validateToken))
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                if (!response.isSuccessful()) {
                    return false;
                } else if (response.isSuccessful()) {
                    String json = response.body().string();
                    JSONObject root = new JSONObject(json);
                    this.user.setUserId(root.getString("_id"));
                    this.user.setUserFirstName(root.getString("firstName"));
                    this.user.setUserLastName(root.getString("lastName"));
                    this.user.setUserEmail(root.getString("email"));
                    this.user.setUserCity(root.getString("city"));
                    this.user.setUserGender(root.getString("gender"));
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
