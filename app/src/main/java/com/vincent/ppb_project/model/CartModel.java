package com.vincent.ppb_project.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartModel implements Parcelable {
    private String hari = "";
    private long startAt = 0, finishAt = 0;

    public CartModel(String hari, long startAt, long finishAt) {
        setStartAt(startAt);
        setFinishAt(finishAt);
        setHari(hari);
    }

    public CartModel() {}


    protected CartModel(Parcel in) {
        hari = in.readString();
        startAt = in.readLong();
        finishAt = in.readLong();
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public long getStartAt() {
        return startAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public long getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(long finishAt) {
        this.finishAt = finishAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hari);
        dest.writeLong(startAt);
        dest.writeLong(finishAt);
    }
}
