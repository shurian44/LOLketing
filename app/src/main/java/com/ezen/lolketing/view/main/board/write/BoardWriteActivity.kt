package com.ezen.lolketing.view.main.board.write

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityBoardWriteBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.view.dialog.CategorySelectDialog
import com.ezen.lolketing.view.dialog.TeamSelectDialog
import com.ezen.lolketing.view.gallery.GalleryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardWriteActivity :
    StatusViewModelActivity<ActivityBoardWriteBinding, BoardWriteViewModel>(R.layout.activity_board_write) {

    override val viewModel: BoardWriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSettings()

    }

    /** 각종 설정 초기화 **/
    private fun initSettings() = with(binding) {
        activity = this@BoardWriteActivity
        vm = viewModel
        title = getString(R.string.insert_board)
        layoutTop.btnBack.setOnClickListener { finish() }
    } // initViews

    fun removeFocus() {
        clearFocus()
    }

    /** 카테고리 선택 다이얼로그 **/
    fun selectTeam() {
        clearFocus()
        TeamSelectDialog(
            isNeedAll = false,
            onClick = viewModel::updateTeam
        ).show(supportFragmentManager, "team")
    }

    /** 이미지 추가 버튼 클릭 **/
    fun addImage() {
        clearFocus()
        launcher.launch(createIntent(GalleryActivity::class.java))
    }

    /** 이미지 제거 **/
    fun removeImage() {
        viewModel.updateImageUri(null)
    }

    /** 포케스 제거 **/
    private fun clearFocus() = with(binding) {
        editContents.clearFocus()
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getParcelableExtra(Constants.SELECT_IMAGE, Uri::class.java)?.let { uri ->
                        viewModel.updateImageUri(uri)
                    }
                } else {
                    it.data?.getParcelableExtra<Uri>(Constants.SELECT_IMAGE)?.let { uri ->
                        viewModel.updateImageUri(uri)
                    }
                }
            }
        }

}
