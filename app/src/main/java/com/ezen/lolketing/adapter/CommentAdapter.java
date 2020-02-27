package com.ezen.lolketing.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezen.lolketing.R;
import com.ezen.lolketing.model.BoardDTO;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class CommentAdapter extends FirestoreRecyclerAdapter<BoardDTO.commentDTO, CommentAdapter.CommentHolder> {

    private String documentID;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public CommentAdapter(@NonNull FirestoreRecyclerOptions<BoardDTO.commentDTO> options, String documentID) {
        super(options);
        this.documentID = documentID;
    }

    @Override
    protected void onBindViewHolder(@NonNull final CommentHolder holder, int position, @NonNull final BoardDTO.commentDTO comment) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timestamp = timeFormat.format(comment.getTimestamp());

        holder.textView_userId.setText(comment.getUserId());
        holder.textView_timestamp.setText(timestamp);
        holder.textView_comment.setText(comment.getComment());
        // 댓글 삭제 버튼 클릭 이벤트
        holder.imageView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDelete(holder.itemView.getContext(), holder.getAdapterPosition(), comment.getEmail());
             }
        });

    } // onBindViewHolder

    public void CommentDelete(final Context context, final int position, final String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("댓글 삭제").setMessage("정말 삭제하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                if(email.equals(auth.getCurrentUser().getEmail())){
                    getSnapshots().getSnapshot(position).getReference().delete();
                    Toast.makeText(context, "댓글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                    FirebaseFirestore.getInstance().collection("Board").document(documentID).update("commentCounts", FieldValue.increment(-1));
                }else{
                    Toast.makeText(context, "삭제 권한이 없습니다", Toast.LENGTH_SHORT).show();
                }
//                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(context, "삭제 취소.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view);
    } // onCreateViewHolder

    public class CommentHolder extends RecyclerView.ViewHolder {
        TextView textView_userId, textView_timestamp, textView_comment;
        ImageView imageView_delete;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            textView_userId = itemView.findViewById(R.id.textView_userId);
            textView_timestamp = itemView.findViewById(R.id.textView_timestamp);
            textView_comment = itemView.findViewById(R.id.textView_comment);
            imageView_delete = itemView.findViewById(R.id.imageView_delete);
        }
    } // CommentHolder

} // class CommentAdapter
