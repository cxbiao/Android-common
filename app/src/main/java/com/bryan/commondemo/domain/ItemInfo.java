package com.bryan.commondemo.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bryan on 2015/8/23.
 */
public class ItemInfo implements Parcelable{

    private String info;
    private int isActivity;
    private String activityName;
    private String fragmentName;

    public  ItemInfo(){

    }
    protected ItemInfo(Parcel in) {
        info = in.readString();
        isActivity = in.readInt();
        activityName = in.readString();
        fragmentName = in.readString();
    }


    public static final Creator<ItemInfo> CREATOR = new Creator<ItemInfo>() {
        @Override
        public ItemInfo createFromParcel(Parcel in) {
            return new ItemInfo(in);
        }

        @Override
        public ItemInfo[] newArray(int size) {
            return new ItemInfo[size];
        }
    };

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int isActivity() {
        return isActivity;
    }

    public void setIsActivity(int isActivity) {
        this.isActivity = isActivity;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(info);
        dest.writeInt(isActivity);
        dest.writeString(activityName);
        dest.writeString(fragmentName);
    }
}
