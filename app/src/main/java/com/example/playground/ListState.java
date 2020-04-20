package com.example.playground;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

final class ListState extends View.BaseSavedState {

    boolean listIsExpanded;

    public ListState(Parcelable superState) {
        super(superState);
    }

    public ListState(Parcel source) {
        super(source);
        listIsExpanded = source.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(listIsExpanded ? 1 : 0);
    }

    public static final Parcelable.Creator<ListState> CREATOR = new Parcelable.Creator<ListState>() {
        public ListState createFromParcel(Parcel in) {
            return new ListState(in);
        }

        public ListState[] newArray(int size) {
            return new ListState[size];
        }
    };
}
