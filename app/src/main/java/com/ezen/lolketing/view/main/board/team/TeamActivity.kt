package com.ezen.lolketing.view.main.board.team

import android.os.Bundle
import androidx.activity.viewModels
import com.ezen.lolketing.BaseViewModelActivity
import com.ezen.lolketing.R
import com.ezen.lolketing.adapter.TeamAdapter
import com.ezen.lolketing.databinding.ActivityTeamBinding
import com.ezen.lolketing.model.PlayerInfo
import com.ezen.lolketing.util.Constants
import com.ezen.lolketing.util.repeatOnStarted
import com.ezen.lolketing.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamActivity : BaseViewModelActivity<ActivityTeamBinding, TeamViewModel>(R.layout.activity_team) {

    override val viewModel: TeamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> eventHandler(event) }
        }

        initViews()

    }

    /** 각종 뷰들 초기화 **/
    private fun initViews() = with(binding) {
        val team = intent?.getStringExtra(Constants.TEAM) ?: kotlin.run {
            toast(R.string.error_unexpected)
            finish()
            return@with
        }

        teamName.text = team
        viewModel.getTeamInfo(team)
    }

    private fun eventHandler(event: TeamViewModel.Event) {
        when(event) {
            is TeamViewModel.Event.Failure -> {
                toast(R.string.error_unexpected)
                finish()
            }
            is TeamViewModel.Event.PlayerList -> {
                setAdapter(event.list)
            }
            is TeamViewModel.Event.Team -> {
                binding.teamFoundation.text = event.info.foundation
            }
        }
    }

    /** adapter 설정 **/
    private fun setAdapter(list: List<PlayerInfo>) = with(binding) {
        val adapter = TeamAdapter().also {
            it.addList(list)
        }
        teamRecycler.adapter = adapter
    }

}