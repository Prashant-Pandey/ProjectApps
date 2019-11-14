package com.mobility.inclass07.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobility.inclass07.R;
import com.mobility.inclass07.SurveyActivity;
import com.mobility.inclass07.listener.TeamActionListener;
import com.mobility.inclass07.adapter.TeamListAdapter;
import com.mobility.inclass07.model.TeamModel;
import com.mobility.inclass07.utilities.AppConstant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class TeamListFragment extends Fragment implements TeamActionListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    ArrayList<TeamModel> teamList = new ArrayList<>();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(AppConstant.TEAM_DB_KEY);
    NavController navController;
    SharedPreferences sharedPreferences;
    String isAdmin;
    ProgressDialog progressDialog;

    public TeamListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TeamListFragment newInstance(String param1, String param2) {
        TeamListFragment fragment = new TeamListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        sharedPreferences = getActivity().getSharedPreferences(getContext().getString(R.string.login_email_shared_preference), Context.MODE_PRIVATE);
        isAdmin = sharedPreferences.getString(getContext().getString(R.string.admin_login_email), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_list, container, false);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        setUpRecyclerView(view);
        setUpAddFloater(view);
    }

    private void setUpAddFloater(View view) {
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        if (isAdmin == null)
            fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.create_new_team, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Add new team");
                alertDialogBuilder.setPositiveButton("ADD", null);
                alertDialogBuilder.setNegativeButton("CANCEL", null);
                alertDialogBuilder.setView(promptsView);

                final EditText chatrommName = promptsView.findViewById(R.id.teamName);

                final AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addTeam(chatrommName.getText().toString());
                                dialog.dismiss();
                            }
                        });

                        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        negativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });

                alertDialog.show();
            }
        });
    }

    private void addTeam(String name) {
        TeamModel chatroom = new TeamModel();
        chatroom.setName(name);
        mRootRef.push().setValue(chatroom);
    }

    private void setUpRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.teamListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TeamListAdapter(teamList, this, getContext());
        recyclerView.setAdapter(mAdapter);
        initTeamList();
    }

    private void initTeamList() {
        progressDialog.show();
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.exists()) {
                    displayTeamList(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Oops! Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTeamList(DataSnapshot dataSnapshot) {
        teamList.clear();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            TeamModel team = child.getValue(TeamModel.class);
            team.setId(child.getKey());
            team.setAvgRatings(calcRatingAvg(team.getRatings()));
            teamList.add(team);
        }
        if (isAdmin != null)
            sortTeamByAvgRating();
        mAdapter.notifyDataSetChanged();
    }

    private void sortTeamByAvgRating() {
        Collections.sort(teamList, new Comparator<TeamModel>() {
            @Override
            public int compare(TeamModel lhs, TeamModel rhs) {
                return Double.compare(rhs.getAvgRatings(), lhs.getAvgRatings());
            }
        });
    }

    private Double calcRatingAvg(Map<String, Double> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Map.Entry<String, Double> avg : ratings.entrySet()) {
            sum += avg.getValue();
        }
        return sum / ratings.size();
    }


    @Override
    public void rateTeam(TeamModel team) {
        goToSurvey(team);
    }

    void goToSurvey(TeamModel team) {
        Intent intent = new Intent(getActivity(), SurveyActivity.class);
        intent.putExtra(AppConstant.TEAM_ID, team.getId());
        intent.putExtra(AppConstant.TEAM_NAME, team.getName());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                sharedPreferences.edit().clear().commit();
                getContext().getSharedPreferences(getString(R.string.login_email_shared_preference), 0).edit().clear().commit();
                navController.navigate(R.id.action_teamListFragment_to_authFragment);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
