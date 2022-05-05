package com.ezen.lolketing.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ezen.lolketing.view.main.league_info.LeagueInfoActivity;
import com.ezen.lolketing.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class OutlineFragment extends Fragment {
    LeagueInfoActivity leagueInfoActivity;

    // 구글 콘솔사이트에서 발급받는 키
    static String API_KEY ="AIzaSyClW1faOpt1RFPgACn4_JkiuiefKaI10eo";
    // 재생할 동영상의 id값
    static String VIDEO_ID = "Q1FM53Blb0I";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outline, container, false);

        YouTubePlayerSupportFragment youTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.youtube_layout, youTubePlayerSupportFragment).commit();

        youTubePlayerSupportFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean wasRestored) {
                if (! wasRestored) {
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.loadVideo(VIDEO_ID);
//                    youTubePlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult error) {
                // YouTube error
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("errorMessage: ", errorMessage);
            }
        });

        return rootView;
    } // onCreateView()

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        leagueInfoActivity = (LeagueInfoActivity) getActivity();
    } // onAttach()

    @Override
    public void onDetach() {
        super.onDetach();
    }

} // end class
