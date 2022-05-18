package com.ezen.lolketing.view.main.chatting

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.view.main.MainActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.ActivityChattinglistBinding
import com.ezen.lolketing.model.GameDTO
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
    @Inject lateinit var auth : FirebaseAuth

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

        val today = getCurrentDateTime().time.timestampToString("yyyy.MM.dd")
        viewModel.getGameData(today)

        viewModel.getUserNickName()

        txtToday.text = today
        year = today.split(".")[0].toInt()
        month = today.split(".")[1].toInt()
        date = today.split(".")[2].toInt()

    }

    fun showDatePickerDialog(view : View) {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                this.year = year
                this.month = month + 1
                this.date = dayOfMonth
                binding.txtToday.text = "$year.${this.month.toString().padStart(2, '0')}.${date.toString().padStart(2, '0')}"
                viewModel.getGameData(binding.txtToday.text.toString())
            },
            year,
            month-1,
            date
        ).show()
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

    private fun setGameData(list: List<GameDTO>) = with(binding) {
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
        var time = binding.txtToday.text.toString().replace(".", "")

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

    override fun logout(view: View) {
        auth.signOut()
        startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    override fun moveHome(view: View) {
        startActivity(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
    }

    companion object {
        const val MINUTE = 1000 * 60
    }

}