package com.ezen.lolketing.view.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ezen.lolketing.view.gallery.detail.GalleryDetailActivity
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityGalleryBinding
import com.ezen.lolketing.model.GalleryItem
import com.ezen.lolketing.util.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GalleryActivity : BaseViewModelActivity<ActivityGalleryBinding, GalleryViewModel>(R.layout.activity_gallery) {

    override val viewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> eventHandler(event)}
        }

        checkPermission()

    }

    private fun eventHandler(event: GalleryViewModel.Event) {
        when(event) {
            is GalleryViewModel.Event.ShowGalleryList -> {
                setGalleryItems(event.list)
            }
        }
    }

    private fun checkPermission() {
        TedPermission
            .create()
            .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    initViews()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    toast("이미지 업로드를 위해 권한을 허용해주세요.")
                    Handler(mainLooper).postDelayed({ finish() }, 1000)
                }
            })
            .check()
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = GalleryAdapter(
            itemClickListener = {
                launch.launch(createIntent(GalleryDetailActivity::class.java).also {
//                    it.putExtra(Constants.POSITION, it)
//                    it.putExtra(Constants.SELECT_IMAGE_LIST, getSelectArrayList())
                })
            },
            itemCountListener = {
                binding.textViewCount.text = "$it"
            }
        )

        textViewComplete.setOnClickListener {
            setResult(RESULT_OK, Intent().also {
                it.putExtra(Constants.SELECT_IMAGE_LIST, getSelectArrayList())
            })
            finish()
        }

        btnClose.setOnClickListener { finish() }

        viewModel.getImageList(application)

    }

    private fun setGalleryItems(list: List<GalleryItem>) = with(binding.recyclerView) {
        (adapter as? GalleryAdapter)?.submitList(list)
    }

    private fun getSelectList() = (binding.recyclerView.adapter as? GalleryAdapter)?.getSelectItemList()

    private fun getSelectArrayList() = listToArrayList(getSelectList())

    private val launch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.getParcelableArrayListExtra<GalleryItem>(Constants.SELECT_IMAGE_LIST)?.let { list ->
                (binding.recyclerView.adapter as? GalleryAdapter)?.apply {
                    setSelectItemList(list)
                    binding.textViewCount.text = "${getSelectList()?.size}"
                }
            }
        }
    }

    override fun logout(view: View) {}

}