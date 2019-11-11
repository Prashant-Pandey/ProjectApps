package com.mobility.inclass07.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobility.inclass07.R;
import com.mobility.inclass07.listener.TeamActionListener;
import com.mobility.inclass07.model.TeamModel;

import java.util.ArrayList;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.ViewHolder> {

    ArrayList<TeamModel> teamArrayList;
    TeamActionListener asyncTask;

    String TAG = "demo";

    public TeamListAdapter(ArrayList<TeamModel> productArrayList, TeamActionListener asyncTask) {
        this.teamArrayList = productArrayList;
        this.asyncTask = asyncTask;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_team_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        TeamModel team = teamArrayList.get(position);
        holder.name.setText(team.getName());

    }

    @Override
    public int getItemCount() {
        return teamArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, ratings, rate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teamNameTextView);
            ratings = itemView.findViewById(R.id.ratingTextView);
            rate = itemView.findViewById(R.id.rateTeam);
            rate.setPaintFlags(rate.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = getLayoutPosition();
                    TeamModel team = teamArrayList.get(p);
                    asyncTask.rateTeam(team);

                }
            });

        }
    }
}
