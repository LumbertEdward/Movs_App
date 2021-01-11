package com.example.movs;

import androidx.recyclerview.widget.RecyclerView;

public interface selectedItemInformationDetailsInterface {
    void getNowShowingSelectedItemInformation(NowPlaying nowPlaying);
    void onBackPressed();
    void getUpcomingSelectedItem(Upcoming upcoming);
    void getPopularSelectedItem(Popular popular);
    void getMore();
    void getUpcomingMore();
    void getPopularMore();
    void getTopRatedMore();
    void getTopRatedItem(TopRated topRated);
    void getGenre(Genres genres);

}
