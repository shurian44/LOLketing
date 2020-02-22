package com.ezen.lolketing.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezen.lolketing.BoardDetailActivity;
import com.ezen.lolketing.R;
import com.ezen.lolketing.model.BoardDTO;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.io.Serializable;
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
    protected void onBindViewHolder(@NonNull final BoardHolder holder, final int position, @NonNull final BoardDTO board) {
//        Long  timestamp = board.getTimestamp();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timestamp = timeFormat.format(board.getTimestamp());

        holder.textView_subject.setText(board.getSubject());
        holder.textView_title.setText(board.getTitle());
        holder.textView_userId.setText(board.getUserId());
        holder.textView_timestamp.setText(timestamp);
        holder.textView_likeCounts.setText("좋아요 " + board.getLikeCounts());
        holder.textView_views.setText("조회수 " + board.getViews());

        holder.textView_commentCounts.setText(board.getCommentCounts() + "\n댓글");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), BoardDetailActivity.class);
                intent.putExtra("subject", board.getSubject());
                intent.putExtra("title", board.getTitle());
                intent.putExtra("userId", board.getUserId());
                intent.putExtra("timestamp", board.getTimestamp());
                intent.putExtra("views", board.getViews());
                intent.putExtra("image", board.getImage());
                intent.putExtra("content", board.getContent());
                intent.putExtra("commentCounts", board.getCommentCounts());
                intent.putExtra("like", (Serializable) board.getLike());
                intent.putExtra("likeCounts", board.getLikeCounts());
                intent.putExtra("documentID", getSnapshots().getSnapshot(position).getId());

                listener.activityMove(intent);
            }
        });

    } // onBindViewHolder

    @NonNull
    @Override
    public BoardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false);
        return new BoardHolder(view);
    } // onCreateViewHolder

    public class BoardHolder extends RecyclerView.ViewHolder {
        TextView textView_subject, textView_title, textView_userId, textView_timestamp, textView_commentCounts
            ,textView_likeCounts, textView_views;

        public BoardHolder(@NonNull View itemView) {
            super(itemView);
            textView_subject = itemView.findViewById(R.id.textView_subject);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_userId = itemView.findViewById(R.id.textView_userId);
            textView_timestamp = itemView.findViewById(R.id.textView_timestamp);
            textView_commentCounts = itemView.findViewById(R.id.textView_commentCounts);
            textView_likeCounts = itemView.findViewById(R.id.textView_likeCounts);
            textView_views = itemView.findViewById(R.id.textView_views);

        }
    } // BoardHolder

    public interface setActivityMove{
        void activityMove(Intent intent);
    }


} // class BoardAdapter
