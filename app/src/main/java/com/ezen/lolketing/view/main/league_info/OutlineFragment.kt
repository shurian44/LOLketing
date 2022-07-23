package com.ezen.lolketing.view.main.league_info

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.ezen.lolketing.R
import androidx.fragment.app.Fragment

class OutlineFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_outline, container, false)
    } // onCreateView()
} // end class
