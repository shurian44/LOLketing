package com.ezen.lolketing.view.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
        activity = this@GalleryActivity
        title = getString(R.string.select_image)

        recyclerView.adapter = GalleryAdapter(
            itemClickListener = { position ->
                launch.launch(
                    createIntent(GalleryDetailActivity::class.java).also {
                        it.putExtra(Constants.POSITION, position)
                        it.putExtra(Constants.SELECT_IMAGE, getSelectItem())
                    }
                )
            }
        )

        btnRegister.setOnClickListener {
            setResult(RESULT_OK, Intent().also {
                it.putExtra(Constants.SELECT_IMAGE, getSelectItem())
            })
            finish()
        }

        layoutTop.btnBack.setOnClickListener { onBackClick(it) }

        viewModel.getImageList(application)

    }

    private fun setGalleryItems(list: List<GalleryItem>) = with(binding.recyclerView) {
        (adapter as? GalleryAdapter)?.submitList(list)
    }

    private fun getSelectItem() = (binding.recyclerView.adapter as? GalleryAdapter)?.getSelectItem()

    private val launch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            val selectItem = it.data?.getParcelableExtra<GalleryItem>(Constants.SELECT_IMAGE)
            (binding.recyclerView.adapter as? GalleryAdapter)?.apply {
                setSelectItem(selectItem)
            }
        }
    }

}