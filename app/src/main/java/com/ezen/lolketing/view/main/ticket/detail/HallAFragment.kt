package com.ezen.lolketing.view.main.ticket.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.FragmentHallABinding
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.custom.CustomSeatCheckBox
import kotlin.math.max

class HallAFragment(
    val changeListener: (String) -> Unit
) : Fragment() {

    private lateinit var binding : FragmentHallABinding
    private val checkBoxList = mutableListOf<CustomSeatCheckBox>()

    private var selectStr = ""
        set(value) {
            field = value
            changeListener(value)
        }
    private var selectList = mutableListOf<String>()

    private var maxCount = 1
        set(value) {
            checkBoxList.forEach { it.isChecked = false }
            field = value
        }
    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHallABinding.inflate(layoutInflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {
        checkBoxList.addAll(
            listOf(
                chA1, chA2, chA3, chA4, chA5, chA6, chA7, chA8,
                chB1, chB2, chB3, chB4, chB5, chB6, chB7, chB8,
                chC1, chC2, chC3, chC4, chC5, chC6, chC7, chC8,
                chD1, chD2, chD3, chD4, chD5, chD6, chD7, chD8,
                chE1, chE2, chE3, chE4, chE5, chE6, chE7, chE8,
                chF1, chF2, chF3, chF4, chF5, chF6, chF7, chF8,
            )
        )

        checkBoxList.forEach {
            it.setOnChangeListener { checked ->
                setCheck(it, checked)
            }
        }
    }

    fun setSeatCount(count : Int) {
        maxCount = count
    }

    private fun setCheck(checkBox: CustomSeatCheckBox, checked: Boolean) {
        when(checked) {
            true -> {
                count++
                if (count > maxCount) {
                    toast("선택한 인원수 보다 많이 선택하였습니다.")
                    checkBox.isChecked = false
                    return
                }
                selectList.add(checkBox.text.toString())
                setStr()
            }
            false -> {
                if (count > 0) count--
                selectList.remove(checkBox.text.toString())
                setStr()
            }
        }
    }

    private fun setStr() {
        var result = ""
        selectList.forEach {
            result += "$it, "
        }
        selectStr = try {
            result.substring(0, result.length-2)
        } catch (e: Exception) {
            getString(R.string.guide_select_seat)
        }
    }

}