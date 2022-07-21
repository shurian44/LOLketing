package com.ezen.lolketing.view.main.chatting

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityChattinglistBinding
import com.ezen.lolketing.model.ChattingInfo
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.custom.CustomChattingItem
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChattingListActivity : BaseViewModelActivity<ActivityChattinglistBinding, ChattingListViewModel>(R.layout.activity_chattinglist) {

    override val viewModel: ChattingListViewModel by viewModels()

    private var year = 2022
    private var month = 1
    private var date = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

        repeatOnStarted {
            viewModel.eventFlow.collect { event -> eventHandler(event) }
        }

    } // onCreate()

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding){

        activity = this@ChattingListActivity
        layoutTop.btnBack.setOnClickListener { finish() }

        viewModel.getUserNickName()
        dateCalculation(isInit = true)
    }

    /** 날짜 계산 후 UI 셋팅 및 경기 일정 조회 **/
    private fun dateCalculation(addDate : Int = 0, isInit: Boolean = false) = with(binding) {
        val cal = Calendar.getInstance().also {
            if (isInit.not()) {
                it.set(year, month, date)
                it.add(Calendar.DATE, addDate)
            } else {
                //
                it.add(Calendar.MONTH, 1)
            }
        }

        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        date = cal.get(Calendar.DATE)

        txtDate.text = "$year.${month.toString().padStart(2, '0')}.${date.toString().padStart(2, '0')}"
        viewModel.getGameData(txtDate.text.toString())
    }

    /** 날짜 선택 다이얼로그 **/
    fun showDatePickerDialog(view : View) {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                this.year = year
                this.month = month + 1
                this.date = dayOfMonth

                dateCalculation()
            },
            year,
            month-1,
            date
        ).show()
    }

    /** 이전 날짜 **/
    fun previousDay(view: View) {
        dateCalculation(-1)
    }

    /** 다음 날짜 **/
    fun nextDay(view: View) {
        dateCalculation(1)
    }

    private fun eventHandler(event: ChattingListViewModel.Event) {
        when(event) {
            // 경기 일정 조회
            is ChattingListViewModel.Event.GameData -> {
                setGameData(event.list)
            }
            // 경기 일정 조회 실패
            is ChattingListViewModel.Event.Error -> {
                toast(R.string.error_game_info)
            }
            // 유저 닉네임 조회 실패
            is ChattingListViewModel.Event.UserNickNameError -> {
                toast(R.string.error_user_info_search)
                Handler(mainLooper).postDelayed({ finish() }, 1000)
            }
        }
    }

    /** 게임 데이터 설정 **/
    private fun setGameData(list: List<ChattingInfo>) = with(binding) {
        layoutGame.removeAllViews()
        isNoGame = list.isEmpty()

        list.forEach {
            val team = it.team.split(":")
            if (team.isEmpty() || team.size < 2) {
                return@forEach
            }

            layoutGame.addView(
                CustomChattingItem(this@ChattingListActivity).also { item ->
                    item.setChattingItem(
                        leftTeam = team[0],
                        rightTeam = team[1],
                        time = it.time
                    ) { select ->
                        val time = "${binding.txtDate.text.toString().replace(".", "")} ${it.time}"
                        enterChatting(it.team, select, time)
                    }
                }
            )
        }
    }

    /** 원하는 팀을 선택해서 채팅방 입장 **/
    private fun enterChatting(
        team: String,
        selectTeam: String,
        time: String
    ) {
        if (viewModel.nickName == null) {
            toast(R.string.error_user_info_search)
            return
        }

        startActivity(
            createIntent(ChattingActivity::class.java).also {
                it.putExtra(ChattingActivity.TEAM, team)
                it.putExtra(ChattingActivity.SELECT_TEAM, selectTeam)
                it.putExtra(ChattingActivity.TIME, time)
                it.putExtra(ChattingActivity.NICKNAME, viewModel.nickName)
            }
        )
    }
}