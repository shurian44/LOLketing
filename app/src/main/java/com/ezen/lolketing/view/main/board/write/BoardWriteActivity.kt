package com.ezen.lolketing.view.main.board.write

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityBoardWriteBinding
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.model.GalleryItem
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.gallery.GalleryActivity
import com.ezen.lolketing.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class BoardWriteActivity : BaseViewModelActivity<ActivityBoardWriteBinding, BoardWriteViewModel>(R.layout.activity_board_write) { // class

    override val viewModel: BoardWriteViewModel by viewModels()
    @Inject lateinit var auth : FirebaseAuth
    @Inject lateinit var storage : FirebaseStorage
    private var team: String? = null
    private var board : Board?= null
    private var isModify : Boolean = false

    private var nickname: String? = null
    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

        initViews()

        viewModel.getUserNickName()

    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        team = intent.getStringExtra(Constants.TEAM)
        board = intent.getParcelableExtra(Constants.BOARD)

        boardTitle.text = team

        // 글 수정 시 이미지 여부 확인 및 등록
        board?.let {
            if (it.image != null && it.image.length > 4) {
                setGlide(boardImage, it.image)
                iconPhoto.isVisible = false
            }
            inputTitle.setText(it.title)
            inputContent.setText(it.content)
            isModify = true
        }

        // 이미지 업로드 클릭 이벤트
        boardImage.setOnClickListener {
            launcher.launch(createIntent(GalleryActivity::class.java))
        }

        // 취소 버튼 클릭 이벤트
        btnCancel.setOnClickListener { finish() }

        // 등록 버튼 클릭 이벤트
        btnSubmit.setOnClickListener {
            if (filePath != null) {
                uploadFile()
            } else {
                uploadBoard(null)
            }
        }
    } // initViews

    private fun eventHandler(event : BoardWriteViewModel.Event) {
        when(event) {
            is BoardWriteViewModel.Event.UserNickName -> {
                nickname = event.nickName
            }
            is BoardWriteViewModel.Event.UploadSuccess -> {
                toast("글이 작성 되었습니다.")
                finish()
            }
            is BoardWriteViewModel.Event.UpdateSuccess -> {
                toast("글이 수정 되었습니다.")
                finish()
            }
            is BoardWriteViewModel.Event.Error -> {
                toast(event.msg)
            }
        }
    }

    // 파일 업로드
    private fun uploadFile() {
        // todo Firebase 한 번에 진행 예정

    } // uploadFile

    // 게시글 세팅
    private fun uploadBoard(downloadUrl: String?) {

        when(isModify) {
            true -> {
                if (nickNameError(board?.documentId)) {
                    return
                }
                val updateData = mutableMapOf<String, Any>(
                    "content" to binding.inputContent.text.toString(),
                    "title" to binding.inputTitle.text.toString(),
                )
                downloadUrl?.let{
                    updateData["image"] = it
                }

                board?.documentId?.let {
                    viewModel.updateBoard(
                        documentId = it,
                        updateData = updateData
                    )
                }
            }
            false -> {
                nickNameError(nickname)
                board = Board(
                    email = auth.currentUser!!.email,
                    title = binding.inputTitle.text.toString(),
                    content = binding.inputContent.text.toString(),
                    userId = nickname,
                    like = HashMap(),
                    image = downloadUrl,
                    subject = "[게시판]",
                    team = team,
                    timestamp = System.currentTimeMillis(),
                    commentCounts= 0,
                    views = 0,
                    likeCounts = 0,
                )

                viewModel.uploadBoard(board)
            }
        }
    }

    private fun nickNameError(nickname: String?) : Boolean {
        if (nickname == null) {
            Handler(mainLooper).postDelayed({ toast("회원 정보를 찾지 못했습니다. 문제가 계속 발생하면 다시 로그인 후 이용해주세요") }, 1000)
            return true
        }
        return false
    }

    override fun logout(view: View) {
        auth.signOut()
    }

    fun moveHome(view: View?) {
        startActivity(createIntent(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val imageArrayList = it.data?.getParcelableArrayListExtra<GalleryItem>(Constants.SELECT_IMAGE_LIST)

            if (imageArrayList.isNullOrEmpty()) {
                return@registerForActivityResult
            }

            setGlide(binding.boardImage, imageArrayList[0].contentUri)
        }
    }

}
