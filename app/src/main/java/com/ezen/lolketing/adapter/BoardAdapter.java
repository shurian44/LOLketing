package com.ezen.lolketing.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezen.lolketing.R;
import com.ezen.lolketing.model.BoardDTO;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.List;

public class BoardAdapter extends FirestoreRecyclerAdapter<BoardDTO, BoardAdapter.BoardHolder> {
    private List<BoardDTO> mDataset;

    setActivityMove listener;

    public BoardAdapter(@NonNull FirestoreRecyclerOptions<BoardDTO> options, setActivityMove listener) {
        super(options);
        this.listener = listener;
    } // BoardAdapter

    @Override
    protected void onBindViewHolder(@NonNull BoardHolder holder, int position, @NonNull BoardDTO board) {

//        Long  timestamp = board.getTimestamp();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy.HH.mm a");
        String timestamp = timeFormat.format(board.getTimestamp());

        int likeCounts = board.getLikeCounts();
        int views = board.getViews();
        int commentCounts = board.getCommentCounts();

        holder.textView_subject.setText(board.getSubject());
        holder.textView_title.setText(board.getTitle());
        holder.textView_userId.setText(board.getUserId());
        holder.textView_timestamp.setText(timestamp);
        holder.textView_commentCounts.setText(commentCounts + "\n댓글");

    } // onBindViewHolder

    @NonNull
    @Override
    public BoardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false);
        return new BoardHolder(view);
    } // onCreateViewHolder

    public class BoardHolder extends RecyclerView.ViewHolder {
        TextView textView_subject, textView_title, textView_userId, textView_timestamp, textView_commentCounts;

        public BoardHolder(@NonNull View itemView) {
            super(itemView);
            textView_subject = itemView.findViewById(R.id.textView_subject);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_userId = itemView.findViewById(R.id.textView_userId);
            textView_timestamp = itemView.findViewById(R.id.textView_timestamp);
            textView_commentCounts = itemView.findViewById(R.id.textView_commentCounts);

        }
    } // BoardHolder

    public interface setActivityMove{
        void activityMove(Intent intent);
    }


} // class BoardAdapter
