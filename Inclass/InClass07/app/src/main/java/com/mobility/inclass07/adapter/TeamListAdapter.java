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
import com.mobility.inclass07.utilities.Auth;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

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
        holder.ratings.setText(calcRatingAvg(team));
        holder.rate.setVisibility(showRateAction(team.getRatings()) ? View.VISIBLE : View.GONE);
    }

    private String calcRatingAvg(TeamModel team) {
        if (team.getRatings() == null || team.getAvgRatings() == 0.0) {
            return "N/A";
        }

        return new DecimalFormat("##.#").format(team.getAvgRatings()) + "/5 \t(" + team.getRatings().size() + ")";
    }

    private boolean showRateAction(Map<String, Double> ratings) {
        String currUserId = Auth.getCurrentUserID();
        if (ratings == null) {
            return true;
        } else if (ratings.get(currUserId) == null) {
            return true;
        }
        return false;
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
