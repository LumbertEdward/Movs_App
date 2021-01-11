package com.example.movs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class upcomingAll implements Parcelable {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Upcoming> upcomingList;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    protected upcomingAll(Parcel in) {
        page = in.readInt();
        upcomingList = in.createTypedArrayList(Upcoming.CREATOR);
        totalResults = in.readInt();
        totalPages = in.readInt();
    }

    public static final Creator<upcomingAll> CREATOR = new Creator<upcomingAll>() {
        @Override
        public upcomingAll createFromParcel(Parcel in) {
            return new upcomingAll(in);
        }

        @Override
        public upcomingAll[] newArray(int size) {
            return new upcomingAll[size];
        }
    };

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Upcoming> getUpcomingList() {
        return upcomingList;
    }

    public void setUpcomingList(List<Upcoming> upcomingList) {
        this.upcomingList = upcomingList;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeTypedList(upcomingList);
        dest.writeInt(totalResults);
        dest.writeInt(totalPages);
    }
}
