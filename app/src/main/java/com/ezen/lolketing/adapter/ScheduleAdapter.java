package com.ezen.lolketing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezen.lolketing.R;
import com.ezen.lolketing.model.GameDTO;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ScheduleAdapter extends FirestoreRecyclerAdapter<GameDTO, ScheduleAdapter.ScheduleHolder> {

    public ScheduleAdapter(@NonNull FirestoreRecyclerOptions<GameDTO> options) {
        super(options);
    }

    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ScheduleHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ScheduleHolder holder, final int position,
                                    @NonNull final GameDTO gameDTO) {

        holder.textViewDate.setText(gameDTO.getDate());
        holder.textViewTime.setText(gameDTO.getTime());
    }


    class ScheduleHolder extends RecyclerView.ViewHolder {

        TextView textViewDate;
        TextView textViewTime;
        TextView team1_name;
        TextView team2_name;
        ImageView team1_logo;
        ImageView team2_logo;

        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            team1_name = itemView.findViewById(R.id.team1_name);
            team2_name = itemView.findViewById(R.id.team2_name);
            team1_logo = itemView.findViewById(R.id.team1_logo);
            team2_logo = itemView.findViewById(R.id.team2_logo);
        }


    }
}