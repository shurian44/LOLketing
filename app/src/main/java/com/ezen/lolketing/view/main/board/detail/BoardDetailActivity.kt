package com.ezen.lolketing.view.main.board.detail

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.CommentAdapter
import com.ezen.lolketing.databinding.ActivityBoardDetailBinding
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.model.BoardDTO
import com.ezen.lolketing.model.BoardDTO.commentDTO
import com.ezen.lolketing.model.Users
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.view.main.board.write.BoardWriteActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import com.ezen.lolketing.model.Board.Comment
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BoardDetailActivity : BaseViewModelActivity<ActivityBoardDetailBinding, BoardDetailViewModel>(R.layout.activity_board_detail) {

    override val viewModel : BoardDetailViewModel by viewModels()
    private var board : Board?= null

    private var documentID: String? = null
    var get_image: String? = null
    var get_content: String? = null
    var team: String? = null
    var auth = FirebaseAuth.getInstance()
    var firestore = FirebaseFirestore.getInstance()
    var query: Query? = null
    var adapter: CommentAdapter? = null
    private var comment: Comment? = null
    private var user: Users? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> eventHandler(event) }
        }

        // 뷰 세팅
        initViews()

    } // onCreate

    private fun eventHandler(event: BoardDetailViewModel.Event) {
        when(event) {
            is BoardDetailViewModel.Event -> {

            }
        }
    }

    // 뷰 메소드 캡슐화
    private fun initViews() = with(binding) {
        board = intent.getParcelableExtra(Constants.BOARD)
        if (board == null) {
            toast("오류가 발생하였습니다.")
            return@with
        }
        documentID = intent.getStringExtra("documentID")

        boardTitle.append(board!!.team)
//        firestore.collection("Board").document(documentID!!).get()
//            .addOnSuccessListener { documentSnapshot ->
//                board = documentSnapshot.toObject(BoardDTO::class.java)
//            }

        // 아이디 옆에 등급 이미지 추가
//        firestore.collection("Users").document(auth.currentUser!!.email!!).get()
//            .addOnSuccessListener { documentSnapshot ->
//                user = documentSnapshot.toObject(Users::class.java)
//                if (like != null && like.containsKey(user!!.nickname) && like[user!!.nickname]!!) {
//                    binding.imgLike.setChecked(true)
//                } else {
//                    binding.imgLike.setChecked(false)
//                }
//                when (user!!.grade) {
//                    "브론0
//                        binding.imgRank.setImageResource(R.drawable.icon_gold)
//                    }
//                    "플래티넘" -> {
//                        binding.imgRank.setImageResource(R.drawable.icon_platinum)
//                    }
//                    "마스터" -> {
//                        binding.imgRank.setImageResource(R.drawable.icon_master)
//                    }
//                }
//            }
//        binding.contentTitle.setText(get_content_title)
//        binding.userId.setText(get_userId)
//        binding.boardContent.setText(get_content)
//
//        // Long타입 timestamp String타입으로 변경하기
//        val date = Date(get_timestamp)
//        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
//        val timestampToString = format.format(date)
//        binding.timestamp.setText(timestampToString)
//        binding.views.setText("조회수 $get_views")
//        binding.txtLikeCount.setText(get_likeCounts.toString() + "")
//        binding.txtCommentCount.setText(get_commentCounts.toString() + "")
//
//        // 사진 미등록 시 뷰(구분 선) 숨김
//        if (get_image == null || get_image!!.length < 1) {
//            binding.boardImg.setVisibility(View.GONE)
//            binding.view2.setVisibility(View.GONE)
//        } else {
//            Glide.with(this).load(get_image).into(binding.boardImg)
//        }

        // 라디오버튼 목록
        val title = arrayOf("부적절한 홍보게시물", "음란성 또는 청소년에게 부적합한 내용", "명예훼손/사생활 침해 및 저작권침해 등")

        // 신고하기 버튼
        binding.imgReport.setOnClickListener {
            val pc = AlertDialog.Builder(this@BoardDetailActivity)
            pc.setTitle("신고하기")
            pc.setSingleChoiceItems(title, 3) { dialog, which -> }
            pc.setPositiveButton("확인") { dialog, which -> dialog.dismiss() }
            pc.setNegativeButton("취소") { dialog, which -> dialog.dismiss() }
            pc.show()
        }

        // 좋아요 버튼 클릭 이벤트
        binding.imgLike.setOnClickListener {
            val likeCount = binding.txtLikeCount.text.toString().toInt()
            if (binding.imgLike.isChecked) {
                firestore.collection("Board").document(documentID!!)
                    .update("like." + user!!.nickname, true, "likeCounts", FieldValue.increment(1))
                binding.txtLikeCount.text = (likeCount + 1).toString() + ""
            } else {
                firestore.collection("Board").document(documentID!!).update(
                    "like." + user!!.nickname,
                    false,
                    "likeCounts",
                    FieldValue.increment(-1)
                )
                binding.txtLikeCount.text = (likeCount - 1).toString() + ""
            }
        }

        // 댓글 등록 버튼
        binding.btnSubmit.setOnClickListener {
            val commentCount = binding.txtCommentCount.text.toString().toInt()
            if (binding.inputComment.length() < 1) {
                Toast.makeText(this@BoardDetailActivity, "댓글 내용이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                setFirestore()
                // 댓글 남길 때 댓글 수 증가
                firestore.collection("Board").document(intent.getStringExtra("documentID")!!)
                    .update("commentCounts", FieldValue.increment(1))
                binding.txtCommentCount.text = (commentCount + 1).toString() + ""
            }
        }

        // 더보기 버튼 클릭 이벤트
        binding.btnMore.setOnClickListener { // 팝업메뉴 생성
            val popup = PopupMenu(applicationContext, binding.btnMore)
            // 팝업메뉴 클릭 이벤트
            popup.menuInflater.inflate(R.menu.menu_board, popup.menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.modify -> {
                        if (auth.currentUser!!.email == board!!.email) {
                            Toast.makeText(applicationContext, "글 수정", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, BoardWriteActivity::class.java)
                            intent.putExtra("title", binding.contentTitle.text.toString())
                            intent.putExtra("image", get_image)
                            intent.putExtra("content", binding.boardContent.text.toString())
                            intent.putExtra("team", team)
                            intent.putExtra("documentId", documentID)
                            intent.putExtra("statement", "modify")
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "글 수정 권한이 없습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                        return@OnMenuItemClickListener true
                    }
                    R.id.delete -> {
                        if (auth.currentUser!!.email == board!!.email) {
                            commentDelete()
                        } else {
                            Toast.makeText(applicationContext, "글 삭제 권한이 없습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                        return@OnMenuItemClickListener true
                    }
                }
                false
            })
            popup.show()
        }
    } // setViews

    // 글 삭제 메소드
    private fun commentDelete() {
        val builder = AlertDialog.Builder(this@BoardDetailActivity)
        builder.setTitle("글 삭제").setMessage("정말 삭제하시겠습니까?")
        builder.setPositiveButton("네") { dialog, id ->
            firestore.collection("Board").document(documentID!!).delete()
            Toast.makeText(this@BoardDetailActivity, "글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        builder.setNegativeButton("아니오") { dialog, id ->
            Toast.makeText(
                this@BoardDetailActivity,
                "삭제가 취소되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    } // CommentDelete

    // 댓글 세팅
    private fun setFirestore() {
//        firestore.collection("Users").document(auth.currentUser!!.email!!).get()
//            .addOnSuccessListener { documentSnapshot ->
//                val user = documentSnapshot.toObject(Users::class.java)
//                commentDTO = commentDTO()
//                commentDTO!!.userId = user!!.nickname
//                commentDTO!!.email = user.id
//                commentDTO!!.timestamp = System.currentTimeMillis()
//                commentDTO!!.comment = input_comment!!.text.toString()
//                firestore.collection("Board").document(documentID!!).collection("Comment")
//                    .document().set(
//                    commentDTO!!
//                ).addOnCompleteListener { task ->
//                    if (task.isComplete) {
//                        Toast.makeText(applicationContext, "댓글이 작성 되었습니다.", Toast.LENGTH_SHORT)
//                            .show()
//                        input_comment.setText(null)
//                    }
//                }
//            }
    } // setFirestore

    // 로그아웃
    override fun logout(view: View) {
        auth.signOut()
    }

    // 로고(홈)
    override fun moveHome(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


} // class
