package com.example.movs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenresAll implements Parcelable {
    @SerializedName("genres")
    private List<Genres> genresList;

    protected GenresAll(Parcel in) {
        genresList = in.createTypedArrayList(Genres.CREATOR);
    }

    public static final Creator<GenresAll> CREATOR = new Creator<GenresAll>() {
        @Override
        public GenresAll createFromParcel(Parcel in) {
            return new GenresAll(in);
        }

        @Override
        public GenresAll[] newArray(int size) {
            return new GenresAll[size];
        }
    };

    public List<Genres> getGenresList() {
        return genresList;
    }

    public void setGenresList(List<Genres> genresList) {
        this.genresList = genresList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(genresList);
    }
}
