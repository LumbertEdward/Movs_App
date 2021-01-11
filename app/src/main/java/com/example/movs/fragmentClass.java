package com.example.movs;

import androidx.fragment.app.Fragment;

public class fragmentClass {
    private String tag;
    private Fragment fragment;

    public fragmentClass(String tag, Fragment fragment) {
        this.tag = tag;
        this.fragment = fragment;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
