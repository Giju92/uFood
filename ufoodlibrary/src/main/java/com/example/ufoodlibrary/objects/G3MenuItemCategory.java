package com.example.ufoodlibrary.objects;


import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Luigi on 06/04/2016.
 */
public class G3MenuItemCategory implements Comparable, Parcelable {

    private String name;
    private int priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public G3MenuItemCategory(){

    }

    public G3MenuItemCategory(int priority, String name) {
        this.priority = priority;
        this.name = name;
    }

    @Override
    public int compareTo(Object another) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if ( this == another ) return EQUAL;

        final G3MenuItemCategory that = (G3MenuItemCategory) another;

        if (this.priority < that.getPriority()) return BEFORE;
        if (this.priority > that.getPriority()) return AFTER;

        int comparison = this.name.compareTo(that.getName());
        if ( comparison != EQUAL ) return comparison;

        return EQUAL;

    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;

        if ( !(o instanceof G3MenuItemCategory) ) return false;

        G3MenuItemCategory that = (G3MenuItemCategory) o;

        if(this.name.equals(that.name) && this.priority == that.priority)
            return true;
        else
            return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.priority);
    }

    protected G3MenuItemCategory(Parcel in) {
        this.name = in.readString();
        this.priority = in.readInt();
    }

    public static final Parcelable.Creator<G3MenuItemCategory> CREATOR = new Parcelable.Creator<G3MenuItemCategory>() {
        @Override
        public G3MenuItemCategory createFromParcel(Parcel source) {
            return new G3MenuItemCategory(source);
        }

        @Override
        public G3MenuItemCategory[] newArray(int size) {
            return new G3MenuItemCategory[size];
        }
    };

}
