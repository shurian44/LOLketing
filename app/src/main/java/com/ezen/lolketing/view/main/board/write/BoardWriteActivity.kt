package com.ezen.lolketing.view.main.board.write

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityBoardWriteBinding
import com.ezen.lolketing.model.BoardWriteInfo
import com.ezen.lolketing.model.GalleryItem
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.dialog.CategorySelectDialog
import com.ezen.lolketing.view.gallery.GalleryActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BoardWriteActivity : BaseViewModelActivity<ActivityBoardWriteBinding, BoardWriteViewModel>(R.layout.activity_board_write) {

    override val viewModel: BoardWriteViewModel by viewModels()

    private var team: String? = null
    private var documentId: String?= null
    private var isModify : Boolean = false
    private var nickname: String? = null
    private var filePath: Uri? = null
    private var isImageChange: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

        initSettings()

    }

    /** 각종 설정 초기화 **/
    private fun initSettings() = with(binding) {
        team = intent.getStringExtra(Constants.TEAM)
        documentId = intent.getStringExtra(Constants.DOCUMENT_ID)
        title = team
        activity = this@BoardWriteActivity

        root.setOnClickListener { clearFocus() }
        layout.setOnClickListener { clearFocus() }
        layoutTop.btnBack.setOnClickListener { onBackClick(it) }

        documentId?.let {
            // 게시글 조회
            viewModel.getBoard(it)
            isModify = true
        }

    } // initViews

    /** 카테고리 선택 다이얼로그 **/
    fun selectCategory(view: View) {
        clearFocus()
        CategorySelectDialog(
            data = binding.editCategory.text.toString()
        ){
            binding.editCategory.setText(it)
        }.show(supportFragmentManager, "category")
    }

    /** 이미지 추가 버튼 클릭 **/
    fun addImage(view: View) {
        clearFocus()
        launcher.launch(createIntent(GalleryActivity::class.java))
    }

    private fun eventHandler(event : BoardWriteViewModel.Event) {
        dismissDialog()
        when(event) {
            is BoardWriteViewModel.Event.Loading -> {
                showDialog()
            }
            is BoardWriteViewModel.Event.WriteInfo -> {
                setModifyInfoViews(event.info)
            }
            is BoardWriteViewModel.Event.UserNickName -> {
                nickname = event.nickName
            }
            is BoardWriteViewModel.Event.UploadSuccess -> {
                toast(R.string.board_write_complete)
                setResult(RESULT_OK)
                finish()
            }
            is BoardWriteViewModel.Event.UpdateSuccess -> {
                toast(R.string.board_modify_complete)
                setResult(RESULT_OK)
                finish()
            }
            is BoardWriteViewModel.Event.Error -> {
                toast(event.msg)
            }
            is BoardWriteViewModel.Event.ImageUploadSuccess -> {
                uploadBoard(event.downloadUri)
            }
        }
    }

    /** 업로드 체크 **/
    fun uploadCheck(view: View) = with(binding) {
        if (editContents.getText().isEmpty()){
            toast(getString(R.string.input_contents))
            return@with
        }
        if (editTitle.text.toString().isEmpty()) {
            toast(R.string.write_title)
            return@with
        }
        if (editCategory.text.toString().isEmpty()) {
            toast(R.string.select_category)
            return@with
        }

        if (filePath != null) {
            uploadFile()
        } else {
            uploadBoard(null)
        }
    }

    /** 파일 업로드 **/
    private fun uploadFile() {
        if (isModify && isImageChange.not()) {
            uploadBoard(null)
            return
        }
        filePath?.let { viewModel.uploadImage(it) } ?: uploadBoard(null)
    } // uploadFile

    /** 게시글 업로드 **/
    private fun uploadBoard(downloadUrl: String?) {

        when(isModify) {
            true -> {
                val updateData = mutableMapOf<String, Any>(
                    "content" to binding.editContents.getText(),
                    "title" to binding.editTitle.text.toString(),
                    "category" to findCode(binding.editCategory.text.toString()),
                )
                downloadUrl?.let{
                    updateData["image"] = it
                }

                documentId?.let {
                    viewModel.updateBoard(
                        documentId = it,
                        updateData = updateData
                    )
                } ?: toast(getString(R.string.error_unexpected))
            }
            false -> {
                viewModel.uploadBoard(
                    title = binding.editTitle.text.toString(),
                    content = binding.editContents.getText(),
                    category = binding.editCategory.text.toString(),
                    image = downloadUrl,
                    team = team
                )
            }
        }
    }

    /** 수정 시 뷰 셋팅 **/
    private fun setModifyInfoViews(writeInfo: BoardWriteInfo) = with(binding) {
        editContents.setText(writeInfo.content)
        editTitle.setText(writeInfo.title)

        writeInfo.image?.let {
            setImage(Uri.parse(it))
        }

        editCategory.setText(findCodeName(writeInfo.category))

    }

    /** 이미지 셋팅 **/
    private fun setImage(uri: Uri) = with(binding) {
        setGlide(imageView, uri)
        filePath = uri
        groupAttach.isVisible = false
        groupImage.isVisible = true
    }

    /** 이미지 제거 **/
    fun removeImage(view: View) = with(binding) {
        filePath = null
        groupAttach.isVisible = true
        groupImage.isVisible = false
    }

    /** 포케스 제거 **/
    private fun clearFocus() = with(binding) {
        editTitle.clearFocus()
        editContents.clearFocus()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val selectItem = it.data?.getParcelableExtra<GalleryItem>(Constants.SELECT_IMAGE)
                ?: return@registerForActivityResult

            setImage(selectItem.contentUri)
            isImageChange = true

        }
    }

}
