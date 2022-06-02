package com.ezen.lolketing.view.main.ticket.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ezen.lolketing.BaseViewModelFragment
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.FragmentHallBinding
import com.ezen.lolketing.model.SeatItem
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HallFragment(
    private val selectHall : String,
    private val gameTime: String,
    val changeListener: (String, ArrayList<String>) -> Unit
) : BaseViewModelFragment<FragmentHallBinding, HallViewModel>(R.layout.fragment_hall) {

    override val viewModel: HallViewModel by viewModels()
    private lateinit var adapter : HallAdapter
    private val positionList = mutableListOf<Int>()

    private var selectStr = ""
        set(value) {
            field = value
            changeListener(value, selectDocumentList)
        }
    private var selectSeatNumList = mutableListOf<String>()
    private var selectDocumentList = arrayListOf<String>()

    private var maxCount = 1
    private var count = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        repeatOnStarted {
            viewModel.eventFlow.collect{ event-> eventHandler(event) }
        }

    }

    private fun initViews() = with(binding) {
        adapter = HallAdapter { position, seat ->
            when(seat.checked) {
                true -> {
                    count++
                    if (count > maxCount) {
                        toast("선택한 인원수 보다 많이 선택하였습니다.")
                        adapter.setChecked(position, false)
                        return@HallAdapter
                    }
                    selectSeatNumList.add("$selectHall ${seat.seatNum}")
                    selectDocumentList.add(seat.documentId)
                    positionList.add(position)
                    setSeatStr()
                }
                false -> {
                    if (count > 0) count--
                    selectSeatNumList.remove("$selectHall ${seat.seatNum}")
                    selectDocumentList.remove(seat.documentId)
                    positionList.remove(position)
                    setSeatStr()
                }
            }
        }

        viewModel.getSeatList(documentId = gameTime, selectHall = selectHall)
    }

    private fun eventHandler(event: HallViewModel.Event) {
        when(event) {
            is HallViewModel.Event.Success -> {
                setRecyclerView(event.list)
            }
            is HallViewModel.Event.Failure -> {
                toast(getString(R.string.error_unexpected))
            }
        }
    }

    private fun setRecyclerView(list: List<SeatItem>) = with(binding) {
        adapter.setSeatList(list)
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
        selectSeatNumList.clear()
        selectDocumentList.clear()
        positionList.clear()
        setSeatStr()
    }

    private fun setSeatStr() {
        if (selectSeatNumList.isEmpty()){
            selectStr = getString(R.string.guide_select_seat)
            return
        }
        var result = ""
        selectSeatNumList.forEach {
            result += "$it, "
        }
        selectStr = try {
            result.substring(0, result.length-2)
        } catch (e: Exception) {
            getString(R.string.guide_select_seat)
        }
    }

}