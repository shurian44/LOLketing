package com.ezen.lolketing.view.main.chatting

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityChattinglistBinding
import com.ezen.lolketing.model.ChattingInfo
import com.ezen.lolketing.model.Game
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ChattingListActivity : BaseViewModelActivity<ActivityChattinglistBinding, ChattingListViewModel>(R.layout.activity_chattinglist) {

    override val viewModel: ChattingListViewModel by viewModels()

    private var teamA : String?= null // 17:00 게임 팀
    private var teamB : String?= null // 20:00 게임 팀
    private var nickName : String?= null

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

    private fun initViews() = with(binding){

        activity = this@ChattingListActivity
        layoutTop.btnBack.setOnClickListener { finish() }

        val today = getCurrentDateTime().time.timestampToString("yyyy.MM.dd")
        viewModel.getGameData(today)
        viewModel.getUserNickName()

        txtDate.text = today
        today.split(".").apply {
            year = get(0).toInt()
            month = get(1).toInt()
            date = get(2).toInt()
        }
    }

    fun showDatePickerDialog(view : View) {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                this.year = year
                this.month = month + 1
                this.date = dayOfMonth

                setDate()
            },
            year,
            month-1,
            date
        ).show()
    }

    fun previousDay(view: View) {
        dateCalculation(-1)
        setDate()
    }

    fun nextDay(view: View) {
        dateCalculation(1)
        setDate()
    }

    private fun dateCalculation(addDate : Int) {
        val cal = Calendar.getInstance().also {
            it.set(year, month, date)
            it.add(Calendar.DATE, addDate)
        }

        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        date = cal.get(Calendar.DATE)
    }

    private fun setDate() {
        binding.txtDate.text = "$year.${this.month.toString().padStart(2, '0')}.${date.toString().padStart(2, '0')}"
        viewModel.getGameData(binding.txtDate.text.toString())
    }

    private fun eventHandler(event: ChattingListViewModel.Event) {
        when(event) {
            is ChattingListViewModel.Event.UserNickName -> {
                nickName = event.nickName
            }
            is ChattingListViewModel.Event.GameData -> {
                setGameData(event.list)
            }
            is ChattingListViewModel.Event.Error -> {
                toast(event.msg)
            }
        }
    }

    private fun setGameData(list: List<ChattingInfo>) = with(binding) {
        isNoGame = true
        if (list.isEmpty()) return@with

        try {
            teamA = list[0].team
            val firstGameList = teamA?.split(":")
            setTeamLogoImageView(imgTeam1, firstGameList?.get(0) ?: Team.T1.name)
            setTeamLogoImageView(imgTeam2, firstGameList?.get(1) ?: Team.T1.name)

            firstGame.isVisible = true
            isNoGame = false
        } catch (e: Exception) {
            e.printStackTrace()
            firstGame.isVisible = false
        }

        try {
            teamB = list[1].team
            val secondGameList = teamB?.split(":")
            setTeamLogoImageView(imgTeam3, secondGameList?.get(0) ?: Team.T1.name)
            setTeamLogoImageView(imgTeam4, secondGameList?.get(1) ?: Team.T1.name)

            secondGame.isVisible = true
            isNoGame = false
        } catch (e: Exception) {
            e.printStackTrace()
            secondGame.isVisible = false
        }
    }

    // 원하는 팀을 선택해서 채팅방 입장
    fun enterChatting(view: View) {
        if (nickName == null) {
            toast("유저 정보에 오류가 발생하였습니다.")
            return
        }

        val intent = createIntent(ChattingActivity::class.java)
        var selectTeam = "" // 선택한 팀의 위치 저장 L or R : DB에 입장한 유저 따로 저장하기 위함
        // ex) 2020.02.05 -> 20200205 수정 : realtimeDatabase 에서 .은 하위 노드로 판단하기 때문에 제거
        var time = binding.txtDate.text.toString().replace(".", "")

        when(view.id){
            R.id.img_team1 ->{
                time += " 17:00"
                selectTeam = "L"
                intent.putExtra(ChattingActivity.TEAM, teamA)
            }
            R.id.img_team2 ->{
                time += " 17:00"
                selectTeam = "R"
                intent.putExtra(ChattingActivity.TEAM, teamA)
            }
            R.id.img_team3 ->{
                time += " 20:00"
                selectTeam = "L"
                intent.putExtra(ChattingActivity.TEAM, teamB)
            }
            R.id.img_team4 ->{
                time += " 20:00"
                selectTeam = "R"
                intent.putExtra(ChattingActivity.TEAM, teamB)
            }
        }

        // 경기 시작 30분 전 까지는 채팅방 입장 불가
        val dateFormat = SimpleDateFormat("yyyyMMdd HH:mm")
        val currentTime = System.currentTimeMillis()
        val gameStartTime = dateFormat.parse(time)?.time

        if (gameStartTime == null) {
            toast("아직 입장하실수 없습니다.")
            return
        }

//        if ((currentTime / DATE) != (gameStartTime / DATE)){
//            toast("당일만 입장 가능합니다.")
//            return
//        }

        val standardTime = gameStartTime - (30 * MINUTE)

        if ( standardTime > currentTime ) {
            toast("아직 입장하실수 없습니다.")
            return
        }

        intent.apply {
            putExtra(ChattingActivity.SELECT_TEAM, selectTeam)
            putExtra(ChattingActivity.TIME, time)
            putExtra(ChattingActivity.NICKNAME, nickName)
        }

        startActivity(intent)
    } // enterChatting()

    companion object {
        const val MINUTE = 1000 * 60
    }

}