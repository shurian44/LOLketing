package com.ezen.lolketing.view.gallery.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.ezen.lolketing.BaseActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityGalleryDetailBinding
import com.ezen.lolketing.model.GalleryItem
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.GalleryUtil

class GalleryDetailActivity : BaseActivity<ActivityGalleryDetailBinding>(R.layout.activity_gallery_detail) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {
        activity = this@GalleryDetailActivity
        title = getString(R.string.select_image)

        viewPager.adapter = GalleryDetailAdapter{
            layoutTop.isVisible = layoutTop.isVisible.not()
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            val adapter = (viewPager.adapter as? GalleryDetailAdapter) ?:return@setOnCheckedChangeListener
            adapter.currentList[viewPager.currentItem].isChecked = isChecked

            if (isChecked) {
                setOneSelect(adapter.currentList, viewPager.currentItem)
            }
        }

        btnBack.setOnClickListener { back() }

        setViewPager()
    }

    private fun setOneSelect(currentList: List<GalleryItem>, position: Int) {
        currentList.forEachIndexed { index, galleryItem ->
            if (galleryItem.isChecked && index != position) {
                galleryItem.isChecked = false
            }
        }
    }

    private fun setViewPager() = with(binding.viewPager){
        val selectImageList = intent.getParcelableExtra<GalleryItem>(Constants.SELECT_IMAGE)
        val position = intent.getIntExtra(Constants.POSITION, 0)

        val adapter = (adapter as? GalleryDetailAdapter)?.apply {
            submitList(GalleryUtil(application).getImageList())
            setSelectItemList(selectImageList)
            setCurrentItem(position, false)
            binding.checkBox.isChecked = currentList[position].isChecked
        }

        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.checkBox.isChecked = adapter?.currentList?.get(currentItem)?.isChecked ?: false

            }
        })

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            back()
            return false
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun back(){
        val intent = Intent().apply {
            val selectItem = (binding.viewPager.adapter as? GalleryDetailAdapter)?.getSelectItem()
            putExtra(Constants.SELECT_IMAGE, selectItem)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onBackClick(view: View) {
        back()
    }

}