package com.example.movs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NowPlayingAll implements Parcelable {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<NowPlaying> nowPlayingList;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;


    protected NowPlayingAll(Parcel in) {
        page = in.readInt();
        nowPlayingList = in.createTypedArrayList(NowPlaying.CREATOR);
        totalResults = in.readInt();
        totalPages = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeTypedList(nowPlayingList);
        dest.writeInt(totalResults);
        dest.writeInt(totalPages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NowPlayingAll> CREATOR = new Creator<NowPlayingAll>() {
        @Override
        public NowPlayingAll createFromParcel(Parcel in) {
            return new NowPlayingAll(in);
        }

        @Override
        public NowPlayingAll[] newArray(int size) {
            return new NowPlayingAll[size];
        }
    };

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<NowPlaying> getNowPlayingList() {
        return nowPlayingList;
    }

    public void setNowPlayingList(List<NowPlaying> nowPlayingList) {
        this.nowPlayingList = nowPlayingList;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
