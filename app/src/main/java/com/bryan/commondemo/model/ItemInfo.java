package com.bryan.commondemo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bryan on 2015/8/23.
 */
public class ItemInfo implements Parcelable{

    public String info;
    public boolean isActivity;
    public String activityName;
    public String fragmentName;

    public  ItemInfo(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.info);
        dest.writeByte(isActivity ? (byte) 1 : (byte) 0);
        dest.writeString(this.activityName);
        dest.writeString(this.fragmentName);
    }

    protected ItemInfo(Parcel in) {
        this.info = in.readString();
        this.isActivity = in.readByte() != 0;
        this.activityName = in.readString();
        this.fragmentName = in.readString();
    }

    public static final Creator<ItemInfo> CREATOR = new Creator<ItemInfo>() {
        public ItemInfo createFromParcel(Parcel source) {
            return new ItemInfo(source);
        }

        public ItemInfo[] newArray(int size) {
            return new ItemInfo[size];
        }
    };
}
