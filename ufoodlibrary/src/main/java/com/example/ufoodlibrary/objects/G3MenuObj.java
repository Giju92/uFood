package com.example.ufoodlibrary.objects;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.ufoodlibrary.utilities.JsonKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Alfonso-LAPTOP on 04/04/2016.
 */
public class G3MenuObj implements Parcelable {

    private long id;
    private ArrayList<G3MenuItem> menuItems;
    private HashMap<String,G3MenuItemCategory> categories;
    private double averagePrice;

    public G3MenuObj(){

    }

    public G3MenuObj(long id){
        this.menuItems = new ArrayList<>();
        this.categories = new HashMap<>();
    }

    public G3MenuObj(long id, ArrayList<G3MenuItem> menuItems, HashMap<String,G3MenuItemCategory> categories) {
        this.id = id;
        this.menuItems = menuItems;
        this.categories = categories;
        this.averagePrice = 0;
    }

    public G3MenuObj(long id, HashMap<String,G3MenuItemCategory> categories) {
        this.id = id;
        this.menuItems = new ArrayList<>();
        this.categories = categories;
        this.averagePrice = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<G3MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<G3MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public HashMap<String,G3MenuItemCategory> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<String,G3MenuItemCategory> categories) {
        this.categories = categories;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    private void updateAveragePrice() {
        int itemCount = menuItems.size();
        double totalMenuPrice = 0;

        for (G3MenuItem it : menuItems) {
            totalMenuPrice += it.getPrice();
        }

        this.averagePrice = totalMenuPrice / itemCount;
    }

    public void addItem(G3MenuItem item) {

        menuItems.add(item);
        updateAveragePrice();

    }

    public void removeItem(G3MenuItem item) {

        menuItems.remove(item);
        updateAveragePrice();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeTypedList(this.menuItems);
        dest.writeInt(this.categories.size());
        for (Map.Entry<String, G3MenuItemCategory> entry : this.categories.entrySet()) {
            dest.writeValue(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeDouble(this.averagePrice);
    }

    protected G3MenuObj(Parcel in) {
        this.id = in.readLong();
        this.menuItems = in.createTypedArrayList(G3MenuItem.CREATOR);
        int size = in.readInt();
        this.categories = new HashMap<String, G3MenuItemCategory>(size);
        for (int i = 0; i < size; i++) {
            String key = (String) in.readValue(String.class.getClassLoader());
            G3MenuItemCategory value = in.readParcelable(G3MenuItemCategory.class.getClassLoader());
            this.categories.put(key, value);
        }
        this.averagePrice = in.readDouble();
    }

    public static final Creator<G3MenuObj> CREATOR = new Creator<G3MenuObj>() {
        @Override
        public G3MenuObj createFromParcel(Parcel source) {
            return new G3MenuObj(source);
        }

        @Override
        public G3MenuObj[] newArray(int size) {
            return new G3MenuObj[size];
        }
    };

}
