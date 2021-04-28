package com.example.ufoodlibrary.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alfonso on 15/07/2016.
 */
public class G3DailyMenu implements Parcelable {

    private ArrayList<G3MenuItem> menuitems = new ArrayList<>();
    private String nameRestaurant;

    public G3DailyMenu() {

    }

    public G3DailyMenu(String s) {
        this.nameRestaurant = s;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public void addItemToMenu(G3MenuItem item){
        this.menuitems.add(item);
    }

    public int getMenuSize(){
        return this.menuitems.size();
    }

    public void clearMenu(){
        this.menuitems.clear();
    }

    public void remoItemAtPosition(int pos){
        this.menuitems.remove(pos);
    }


    public void setMenuitems(ArrayList<G3MenuItem> menuitems) {
        this.menuitems = menuitems;
    }

    public List<G3MenuItem> getMenuitems(){
        return this.menuitems;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeTypedList(this.menuitems);
        dest.writeString(this.nameRestaurant);
    }

    protected G3DailyMenu(Parcel in) {
        this.menuitems = in.createTypedArrayList(G3MenuItem.CREATOR);
        this.nameRestaurant = in.readString();
    }

    public static final Parcelable.Creator<G3DailyMenu> CREATOR = new Parcelable.Creator<G3DailyMenu>() {
        @Override
        public G3DailyMenu createFromParcel(Parcel source) {
            return new G3DailyMenu(source);
        }

        @Override
        public G3DailyMenu[] newArray(int size) {
            return new G3DailyMenu[size];
        }
    };
}
