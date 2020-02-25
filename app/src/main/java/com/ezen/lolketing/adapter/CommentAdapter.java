package com.ezen.lolketing.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezen.lolketing.R;
import com.ezen.lolketing.model.BoardDTO;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class CommentAdapter extends FirestoreRecyclerAdapter<BoardDTO.commentDTO, CommentAdapter.CommentHolder> {

    private String documentID;

    public CommentAdapter(@NonNull FirestoreRecyclerOptions<BoardDTO.commentDTO> options, String documentID) {
        super(options);
        this.documentID = documentID;
    }

    @Override
    protected void onBindViewHolder(@NonNull final CommentHolder holder, int position, @NonNull BoardDTO.commentDTO comment) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timestamp = timeFormat.format(comment.getTimestamp());

        holder.textView_userId.setText(comment.getUserId());
        holder.textView_timestamp.setText(timestamp);
        holder.textView_comment.setText(comment.getComment());
        // 더보기 메뉴 클릭 -> 수정, 삭제 선택 드랍메뉴
        holder.imageView_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.imageView_more);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_comment, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.modify:
                                Toast.makeText(holder.itemView.getContext(),"댓글 수정", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.delete:
                                Toast.makeText(holder.itemView.getContext(),"댓글 삭제", Toast.LENGTH_SHORT).show();
                                CommentDelete(holder.itemView.getContext(), holder.getAdapterPosition());
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
             }
        });

    } // onBindViewHolder

    public void CommentDelete(final Context context, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("댓글 삭제").setMessage("정말 삭제하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                getSnapshots().getSnapshot(position).getReference().delete();
                Toast.makeText(context, "댓글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                FirebaseFirestore.getInstance().collection("Board").document(documentID).update("commentCounts", FieldValue.increment(-1));
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
        ImageView imageView_more;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            textView_userId = itemView.findViewById(R.id.textView_userId);
            textView_timestamp = itemView.findViewById(R.id.textView_timestamp);
            textView_comment = itemView.findViewById(R.id.textView_comment);
            imageView_more = itemView.findViewById(R.id.imageView_more);
        }
    } // CommentHolder

} // class CommentAdapter
