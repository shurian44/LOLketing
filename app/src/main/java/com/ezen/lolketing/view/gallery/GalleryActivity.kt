package com.ezen.lolketing.view.gallery

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.ezen.lolketing.R
import com.ezen.lolketing.StatusViewModelActivity
import com.ezen.lolketing.databinding.ActivityGalleryBinding
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.gallery.detail.GalleryDetailAdapter
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity :
    StatusViewModelActivity<ActivityGalleryBinding, GalleryViewModel>(R.layout.activity_gallery) {

    override val viewModel: GalleryViewModel by viewModels()
    private var isRegister = false
    val adapter = GalleryAdapter(
        onCheckedClick = {
            viewModel.onSelectChanged(it)
        },
        onItemClick = {
            viewModel.updateIsDetail(true)
            binding.viewPager.setCurrentItem(it, false)
        }
    )

    val detailAdapter = GalleryDetailAdapter {
        viewModel.updateIsDetail(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission()
        initViews()
    }

    private fun checkPermission() {
        TedPermission
            .create()
            .setPermissions(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    android.Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                }
            )
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    viewModel.fetchImageList(application)
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
        vm = viewModel
        title = getString(R.string.select_image)

        layoutTop.btnBack.setOnClickListener { finish() }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.checkBox.isChecked = viewModel.list.value[position].isChecked
            }
        })
    }

    fun onItemSelect() {
        viewModel.onSelectChanged(binding.viewPager.currentItem)
    }

    fun onRegister() {
        viewModel.selectUri?.let { uri ->
            setResult(
                RESULT_OK,
                Intent().also {
                    it.putExtra(Constants.SELECT_IMAGE, uri)
                }
            )
        }
        isRegister = true
        finish()
    }

    override fun finish() {
        if (isRegister.not() && viewModel.isDetail.value) {
            viewModel.updateIsDetail(false)
        } else {
            super.finish()
        }
    }

}