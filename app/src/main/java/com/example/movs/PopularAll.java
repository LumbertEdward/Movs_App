package com.example.movs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularAll implements Parcelable {
    @SerializedName("results")
    private List<Popular> popularList;
    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int total_results;
    @SerializedName("total_pages")
    private int total_pages;

    protected PopularAll(Parcel in) {
        popularList = in.createTypedArrayList(Popular.CREATOR);
        page = in.readInt();
        total_results = in.readInt();
        total_pages = in.readInt();
    }

    public static final Creator<PopularAll> CREATOR = new Creator<PopularAll>() {
        @Override
        public PopularAll createFromParcel(Parcel in) {
            return new PopularAll(in);
        }

        @Override
        public PopularAll[] newArray(int size) {
            return new PopularAll[size];
        }
    };

    public List<Popular> getPopularList() {
        return popularList;
    }

    public void setPopularList(List<Popular> popularList) {
        this.popularList = popularList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(popularList);
        dest.writeInt(page);
        dest.writeInt(total_results);
        dest.writeInt(total_pages);
    }
}
