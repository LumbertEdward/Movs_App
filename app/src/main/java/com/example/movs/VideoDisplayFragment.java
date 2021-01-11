package com.example.movs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


public class VideoDisplayFragment extends Fragment implements YouTubePlayer.OnInitializedListener{
    private String key = "AIzaSyCEuBhYMutH-fNlaiJgWNH96NNDhjep8uk";
    private YouTubePlayerSupportFragment youTubePlayerSupportFragment;
    private TrailersInfo trailersInfo;
    private ImageView imageView;
    private selectedItemInformationDetailsInterface select;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            trailersInfo = bundle.getParcelable("MVideo");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video_display, container, false);
        imageView = (ImageView) v.findViewById(R.id.imgVideoBack);
        youTubePlayerSupportFragment = new YouTubePlayerSupportFragment();
        youTubePlayerSupportFragment.initialize(key, this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_video, youTubePlayerSupportFragment);
        fragmentTransaction.commit();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.onBackPressed();
            }
        });
        return v;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(trailersInfo.getKey());
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof selectedItemInformationDetailsInterface){
            select = (selectedItemInformationDetailsInterface) context;
        }
        else {
            throw new ClassCastException(context.toString() + "Must implement selectedItemInformationDetailsInterface");
        }
    }
}
