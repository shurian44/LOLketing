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

        documentId?.let {
            // 게시글 조회
            viewModel.getBoard(it)
            isModify = true
        }

    } // initViews

    fun selectCategory(view: View) {
        CategorySelectDialog(
            data = binding.editCategory.text.toString()
        ){
            binding.editCategory.setText(it)
        }.show(supportFragmentManager, "category")
    }

    fun addImage(view: View) {
        launcher.launch(createIntent(GalleryActivity::class.java))
    }

    private fun eventHandler(event : BoardWriteViewModel.Event) {
        when(event) {
            is BoardWriteViewModel.Event.WriteInfo -> {
                setModifyInfoViews(event.info)
            }
            is BoardWriteViewModel.Event.UserNickName -> {
                nickname = event.nickName
            }
            is BoardWriteViewModel.Event.UploadSuccess -> {
                toast("글이 작성 되었습니다.")
                setResult(RESULT_OK)
                finish()
            }
            is BoardWriteViewModel.Event.UpdateSuccess -> {
                toast("글이 수정 되었습니다.")
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

    fun upload(view: View) = with(binding) {
        if (editContents.getText().isEmpty() || editTitle.text.toString().isEmpty()
            || editCategory.text.toString().isEmpty()){
            toast(getString(R.string.input_contents))
            return@with
        }

        if (filePath != null) {
            uploadFile()
        } else {
            uploadBoard(null)
        }
    }

    // 파일 업로드
    private fun uploadFile() {
        if (isModify && isImageChange.not()) {
            uploadBoard(null)
            return
        }
        filePath?.let { viewModel.uploadImage(it) } ?: uploadBoard(null)
    } // uploadFile

    // 게시글 세팅
    private fun uploadBoard(downloadUrl: String?) {

        when(isModify) {
            true -> {
                val updateData = mutableMapOf<String, Any>(
                    "content" to binding.editContents.getText(),
                    "title" to binding.editTitle.text.toString(),
                    "category" to binding.editCategory.text.toString(),
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

    private fun setModifyInfoViews(writeInfo: BoardWriteInfo) = with(binding) {
        editContents.setText(writeInfo.content)
        editTitle.setText(writeInfo.title)

        writeInfo.image?.let {
            setImage(Uri.parse(it))
        }

        val category = when(writeInfo.category) {
            Code.QUESTION_BOARD.code -> {
                Code.QUESTION_BOARD.codeName
            }
            Code.GAME_BOARD.code -> {
                Code.GAME_BOARD.codeName
            }
            else -> {
                Code.FREE_BOARD.codeName
            }
        }
        editCategory.setText(category)

    }

    private fun setImage(uri: Uri) = with(binding) {
        setGlide(imageView, uri)
        filePath = uri
        groupAttach.isVisible = false
        groupImage.isVisible = true
    }

    fun removeImage(view: View) = with(binding) {
        filePath = null
        groupAttach.isVisible = true
        groupImage.isVisible = false
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
