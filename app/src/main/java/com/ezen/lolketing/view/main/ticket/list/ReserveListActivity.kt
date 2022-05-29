package com.ezen.lolketing.view.main.ticket.list

import android.os.Bundle
import android.view.View
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
import kotlinx.coroutines.flow.collect

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
        when(event) {
            is ReserveListViewModel.Event.EmptyList -> {
                binding.txtNotTicket.isVisible = true
                binding.reserveRecycler.isVisible = false
            }
            is ReserveListViewModel.Event.GameList -> {
                setRecyclerview(event.list)
            }
            is ReserveListViewModel.Event.UserGrade -> {
                binding.btnAddGame.isVisible = event.isMaster
            }
            is ReserveListViewModel.Event.NewGameAddSuccess -> {
                toast(getString(R.string.add_game_success))
                viewModel.getGameList()
            }
            is ReserveListViewModel.Event.NewGameAddFailure -> {
                toast(getString(R.string.add_game_failure))
            }
        }
    }

    private fun initViews() = with(binding) {
        activity = this@ReserveListActivity
        viewModel.getGameList()
        viewModel.isMasterUser()

        val adapter = ReserveAdapter { ticket ->
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
                    startActivity(
                        createIntent(ReserveDetailActivity::class.java).also {
                            it.putExtra(Constants.TIME, "${ticket.date} ${ticket.time}")
                            it.putExtra(Constants.TEAM, ticket.team)
                        }
                    )
                }
            }

        }

        reserveRecycler.adapter = adapter

    }

    fun addNewGame(view: View) {
        AddNewGameDialog{ date, time ->
            viewModel.addNewGame(date, time)
        }.show(supportFragmentManager, null)
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.getGameList()
    }

    private fun setRecyclerview(list: List<Ticket>) = with(binding) {
        txtNotTicket.isVisible = false
        reserveRecycler.isVisible = true

        (reserveRecycler.adapter as? ReserveAdapter)?.apply {
            submitList(list)
        }
    }

}