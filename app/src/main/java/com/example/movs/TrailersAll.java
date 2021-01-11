package com.example.movs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersAll implements Parcelable {
    @SerializedName("results")
    private List<TrailersInfo> trailersInfoList;
    @SerializedName("id")
    private int id;

    protected TrailersAll(Parcel in) {
        trailersInfoList = in.createTypedArrayList(TrailersInfo.CREATOR);
        id = in.readInt();
    }

    public static final Creator<TrailersAll> CREATOR = new Creator<TrailersAll>() {
        @Override
        public TrailersAll createFromParcel(Parcel in) {
            return new TrailersAll(in);
        }

        @Override
        public TrailersAll[] newArray(int size) {
            return new TrailersAll[size];
        }
    };

    public List<TrailersInfo> getTrailersInfoList() {
        return trailersInfoList;
    }

    public void setTrailersInfoList(List<TrailersInfo> trailersInfoList) {
        this.trailersInfoList = trailersInfoList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(trailersInfoList);
        dest.writeInt(id);
    }
}
