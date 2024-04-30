package com.ezen.lolketing.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.ezen.lolketing.databinding.DialogTeamBinding
import com.ezen.lolketing.util.backgroundTransparent
import com.ezen.lolketing.util.dialogResize
import com.ezen.network.model.Team
import kotlinx.coroutines.flow.MutableStateFlow

class TeamSelectDialog(
    private val isNeedAll: Boolean,
    private val onClick: (Team) -> Unit
) : DialogFragment() {

    private val binding : DialogTeamBinding by lazy { DialogTeamBinding.inflate(layoutInflater) }

    val adapter = TeamAdapter {
        onClick(it)
        dismiss()
    }
    val list = MutableStateFlow(Team.values().toList().drop(1))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 다이얼로그 배경 투명화
        dialog?.backgroundTransparent()
        binding.dialog = this
        if (isNeedAll) {
            binding.btnAllSelect.setOnClickListener {
                onClick(Team.ALL)
                dismiss()
            }
        } else {
            binding.btnAllSelect.isVisible = false
        }

    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogResize(dialog)
    }

}