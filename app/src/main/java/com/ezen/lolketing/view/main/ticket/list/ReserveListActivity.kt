package com.ezen.lolketing.view.main.ticket.list

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.ReserveAdapter
import com.ezen.lolketing.databinding.ActivityReserveListBinding
import com.ezen.lolketing.model.Ticket
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.dialog.AddNewGameDialog
import com.ezen.lolketing.view.main.ticket.detail.ReserveDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReserveListActivity : BaseViewModelActivity<ActivityReserveListBinding, ReserveListViewModel>(R.layout.activity_reserve_list){

    override val viewModel: ReserveListViewModel by viewModels()
    lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

    } // onCreate()

    private fun eventHandler(event : ReserveListViewModel.Event) {
        dismissDialog()

        when(event) {
            // 로딩 다이얼로그 표시
            is ReserveListViewModel.Event.Loading -> {
                showDialog()
            }
            // 경기 조회 결과
            is ReserveListViewModel.Event.GameList -> {
                setRecyclerview(event.list)
            }
            // 유저 등급 조회 : 마스터의 경우 경기 추가 기능 사용
            is ReserveListViewModel.Event.UserGrade -> {
                binding.btnAddGame.isVisible = event.isMaster
            }
            // 경기 추가 성공
            is ReserveListViewModel.Event.NewGameAddSuccess -> {
                viewModel.getGameList()
                viewModel.setReservedSeat(event.time)
            }
            // 경기 추가 실패
            is ReserveListViewModel.Event.NewGameAddFailure -> {
                toast(getString(R.string.add_game_failure))
            }
            // 좌석 추가 완료
            is ReserveListViewModel.Event.ReserveSeatAddSuccess -> {
                toast(getString(R.string.add_game_success))
            }
        }
    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        activity = this@ReserveListActivity
        viewModel.getGameList()
        viewModel.isMasterUser()

        reserveRecycler.adapter = setAdapter()
    }

    /** 어뎁터 설정 **/
    private fun setAdapter() = ReserveAdapter { ticket ->
        when(ticket.status){
            // 매진
            Code.TICKETING_SOLD_OUT.code->{
                toast(getString(R.string.guide_sold_out))
                return@ReserveAdapter
            }
            // 종료
            Code.TICKETING_FINISH.code->{
                toast(getString(R.string.guide_finish))
                return@ReserveAdapter
            }
            // 오픈 예정
            Code.TICKETING_SCHEDULE_OPEN.code->{
                toast(getString(R.string.guide_schedule_open))
                return@ReserveAdapter
            }
            // 예매
            Code.TICKETING_ON.code -> {
                launcher.launch(
                    createIntent(ReserveDetailActivity::class.java).also {
                        it.putExtra(Constants.TIME, "${ticket.date} ${ticket.time}")
                        it.putExtra(Constants.TEAM, ticket.team)
                    }
                )
            }
        }
    }

    /** 리사이클러뷰 설정 **/
    private fun setRecyclerview(list: List<Ticket>) = with(binding) {
        txtNotTicket.isVisible = list.isEmpty()
        reserveRecycler.isVisible = list.isEmpty().not()

        (reserveRecycler.adapter as? ReserveAdapter)?.apply {
            submitList(list)
        }
    }

    /** 경기 추가 **/
    fun addNewGame(view: View) {
        AddNewGameDialog{ date, time ->
            viewModel.addNewGame(date, time)
        }.show(supportFragmentManager, null)
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.getGameList()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

}