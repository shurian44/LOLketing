package com.ezen.lolketing.view.main.ticket.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.FragmentHallBinding
import com.ezen.lolketing.model.SeatItem
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HallFragment(
    private val selectHall : String,
    val changeListener: (String) -> Unit
) : Fragment() {

    private lateinit var binding : FragmentHallBinding
    private lateinit var adapter : HallAdapter
    private val positionList = mutableListOf<Int>()

    private var selectStr = ""
        set(value) {
            field = value
            changeListener(value)
        }
    private var selectList = mutableListOf<String>()

    private var maxCount = 1
    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHallBinding.inflate(layoutInflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() = with(binding) {
        txtTitle.text = "${selectHall}홀"

        adapter = HallAdapter { position, checked, seatNum ->
            when(checked) {
                true -> {
                    count++
                    if (count > maxCount) {
                        toast("선택한 인원수 보다 많이 선택하였습니다.")
                        adapter.setChecked(position, false)
                        return@HallAdapter
                    }
                    selectList.add("${binding.txtTitle.text} $seatNum")
                    positionList.add(position)
                    setStr()
                }
                false -> {
                    if (count > 0) count--
                    selectList.remove("${binding.txtTitle.text} $seatNum")
                    positionList.remove(position)
                    setStr()
                }
            }
        }.also {
            val list = mutableListOf<SeatItem>()

            getAHall().forEach { seatNum ->
                list.add(SeatItem(seatNum = seatNum))
            }

            it.setSeatList(list)
        }
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(GridLayoutSpacing())
    }

    fun setSeatCount(count : Int) {
        maxCount = count
        // 초기화
        this.count = 0
        positionList.forEach {
            adapter.setChecked(it, false)
        }
        selectList.clear()
        positionList.clear()
        setStr()
    }

    private fun setStr() {
        if (selectList.isEmpty()){
            selectStr = getString(R.string.guide_select_seat)
            return
        }
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

    private fun getAHall() : List<String> {
        val list = mutableListOf<String>()
        rowList.forEach {
            for (i in 1..9) {
                list.add("$it$i")
            }
        }
        return list
    }

    companion object {
        val rowList = listOf("A", "B", "C", "D", "E", "F", "G", "H")
    }

}