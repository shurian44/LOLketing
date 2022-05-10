package com.ezen.lolketing.view.dialog

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import com.ezen.lolketing.R

object BoardPopup {

    fun createPopup(
        view : View,
        modifyClickListener : () -> Unit,
        deleteClickListener : () -> Unit
    ) {
        val popup = PopupMenu(view.context, view)
        popup.menuInflater.inflate(R.menu.menu_board, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.modify -> {
                    modifyClickListener()
                    return@setOnMenuItemClickListener true
                }
                R.id.delete -> {
                    deleteClickListener()
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }

        popup.show()
    }
}

//                PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.itemView);
//                popup.getMenuInflater().inflate(R.menu.menu_board, popup.getMenu());
//                // 팝업메뉴 클릭 이벤트
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.modify:
//                                if(auth.getCurrentUser().getEmail().equals(board.getEmail())){
//                                    Toast.makeText(holder.itemView.getContext(),"글 수정", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(holder.itemView.getContext(), BoardWriteActivity.class);
//                                    intent.putExtra("title", board.getTitle());
//                                    intent.putExtra("image", board.getImage());
//                                    intent.putExtra("content", board.getContent());
//                                    intent.putExtra("team", board.getTeam());
//                                    intent.putExtra("timestamp", board.getTimestamp());
//                                    intent.putExtra("documentId", getSnapshots().getSnapshot(position).getId());
//                                    intent.putExtra("statement", "modify");
//                                    listener.activityMove(intent);
//                                }else {
//                                    Toast.makeText(holder.itemView.getContext(),"글 수정 권한이 없습니다.", Toast.LENGTH_SHORT).show();
//                                }
//                                return true;
//                            case R.id.delete:
//                                if(auth.getCurrentUser().getEmail().equals(board.getEmail())) {
//                                    Toast.makeText(holder.itemView.getContext(), "글 삭제", Toast.LENGTH_SHORT).show();
//                                    CommentDelete(holder.itemView.getContext(), holder.getAdapterPosition());
//                                }else {
//                                    Toast.makeText(holder.itemView.getContext(),"글 삭제 권한이 없습니다.", Toast.LENGTH_SHORT).show();
//                                }
//                                return true;
//                        }
//                        return false;
//                    }
//                });
//                popup.show();