package com.ezen.lolketing.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezen.lolketing.R;
import com.ezen.lolketing.model.Game;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ScheduleAdapter extends FirestoreRecyclerAdapter<Game, ScheduleAdapter.ScheduleHolder> {

    public ScheduleAdapter(@NonNull FirestoreRecyclerOptions<Game> options) {
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
                                    @NonNull final Game gameDTO) {

        holder.textViewDate.setText(gameDTO.getDate());
        holder.textViewTime.setText(gameDTO.getTime());

        String [] teams = gameDTO.getTeam().split(":");
        // split("") : "" 안에 들어가는 걸 기준으로 왼쪽 오른쪽을 나눠서 배열에 넣는다.
        Log.e("test", "" + position);

        holder.team1_name.setText(teams[0]);
        setImage(teams[0], holder.team1_logo);
        holder.team2_name.setText(teams[1]);
        setImage(teams[1], holder.team2_logo);
        // Drawable에 이미지가 있기 때문에 Glide를 사용하지 않아도 됨.
    }

    void setImage(String team, ImageView image) {
        Log.e("test", team);
        // 주의! DB하고 대소문자가 다르면 결과가 제대로 안나올 수 있음.
        if (team.equals("T1")) {
            image.setImageResource(R.drawable.logo_t1);
        }
        if (team.equals("Griffin")) {
            image.setImageResource(R.drawable.icon_griffin);
        }
        if (team.equals("DAMWON Gaming")) {
            image.setImageResource(R.drawable.icon_damwon);
        }
        if (team.equals("SANDBOX Gaming")) {
            image.setImageResource(R.drawable.icon_sandbox);
        }
        if (team.equals("Afreeca Freecs")) {
            image.setImageResource(R.drawable.icon_afreeca);
        }
        if (team.equals("Gen.G Esports")) {
            image.setImageResource(R.drawable.icon_geng);
        }
        if (team.equals("DragonX")) {
            image.setImageResource(R.drawable.icon_dragonx);
        }
        if (team.equals("KT Rolster")) {
            image.setImageResource(R.drawable.icon_rolster);
        }
        if (team.equals("APK PRINCE")) {
            image.setImageResource(R.drawable.icon_apk_prince);
        }
        if (team.equals("Hanwha Life Esports")) {
            image.setImageResource(R.drawable.icon_hanwha);
        }
    } // setImage()

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
    } // class ScheduleHolder
} // end class