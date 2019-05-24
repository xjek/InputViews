package com.eugene.inputviews.inputView.savedState;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import java.util.Date;

/**
 * Сохранения состояния
 */
public class DataSavedState extends View.BaseSavedState {
    public String string;
    public int integer;
    public Date date;

    public static DataSavedState setString(String string, Parcelable parcelable) {
        DataSavedState state = new DataSavedState(parcelable);
        state.string = string;
        return state;
    }

    public static DataSavedState setInteger(int integer, Parcelable parcelable) {
        DataSavedState state = new DataSavedState(parcelable);
        state.integer = integer;
        return state;
    }

    public static DataSavedState setDate(Date date, Parcelable parcelable) {
        DataSavedState state = new DataSavedState(parcelable);
        state.date = date;
        return state;
    }

    public DataSavedState(Parcelable parcelable) {
        super(parcelable);
    }

    protected DataSavedState(Parcel in) {
        super(in);
        this.string = in.readString();
        this.integer = in.readInt();
        long date = in.readLong();
        this.date = date == 0 ? null : new Date(date);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(string);
        dest.writeInt(integer);
        dest.writeLong(date == null ? 0 : date.getTime());
    }

    public static final Creator<DataSavedState> CREATOR = new Creator<DataSavedState>() {
        @Override
        public DataSavedState createFromParcel(Parcel in) {
            return new DataSavedState(in);
        }

        @Override
        public DataSavedState[] newArray(int size) {
            return new DataSavedState[size];
        }
    };
}
