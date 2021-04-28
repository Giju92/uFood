package com.example.ufoodlibrary.objects;



import android.os.Parcel;
import android.os.Parcelable;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.utilities.G3Application;

/**
 * Created by Alfonso on 01-Apr-16.
 */
public class G3MenuItem implements Parcelable {

    private long id;
    private int salePercentage;
    private int typeofItem;
    private boolean available;
    private double price;
    private boolean vegan;
    private boolean vegetarian;
    private boolean glutenFree;
    private String name;
    private String category;
    private String items;
    private String additionalClause;
    private String ingredients;


    private static final String CATEGORY_DRINK = G3Application.getAppContext().getString(R.string.drink);
    private static final String CATEGORY_SIDE = G3Application.getAppContext().getString(R.string.side);
    private static final String CATEGORY_DESSERT = G3Application.getAppContext().getString(R.string.dessert);
    private static final String CATEGORY_APPETIZER = G3Application.getAppContext().getString(R.string.appetizer);
    private static final String CATEGORY_WINE = G3Application.getAppContext().getString(R.string.wine);
    private static final String CATEGORY_HOTDRINK = G3Application.getAppContext().getString(R.string.hot_drink);
    private static final String CATEGORY_PIZZA = G3Application.getAppContext().getString(R.string.pizza);

    public static enum DishCategory {

        DRINK(0,CATEGORY_DRINK),
        DESSERT(1,CATEGORY_DESSERT),
        APPETIZER(2,CATEGORY_APPETIZER),
        WINE(3,CATEGORY_WINE),
        HOTDRINK(4,CATEGORY_HOTDRINK),
        SIDES(5,CATEGORY_SIDE),
        PIZZA(6,CATEGORY_PIZZA);

        private String str;
        private int num;

        DishCategory(int num,String str) {
            this.str = new String(str);
            this.num = num;
        }

        public String getString() {

            return new String(this.str);
        }

        public int getNumber() {

            return this.num;
        }

        public static DishCategory getDishCategoryFromString(String str) {

            DishCategory rVal = null;

            if (str != null) {
                for (DishCategory ie : DishCategory.values()) {
                    if (ie.getString().compareTo(str) == 0) {
                        rVal = ie;
                        break;
                    }
                }
            }

            return rVal;
        }

        public static DishCategory getDishCategoryFromInt(int n) {

            DishCategory rVal = null;

            if (n >= 0) {
                for (DishCategory ie : DishCategory.values()) {
                    if (ie.getNumber() == n) {
                        rVal = ie;
                        break;
                    }
                }
            }

            return rVal;
        }
    }

    public G3MenuItem(){

    }

    //Constructor for drinks
    public G3MenuItem(long id, String name, double price, int salePercentage, String category, int typeofItem,boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.salePercentage = salePercentage;
        this.category = category;
        this.typeofItem = typeofItem;
        this.available = available;
    }

    //Constructor for foods
    public G3MenuItem(long id, String name, double price, int salePercentage, String category,
                      String ingredients, boolean vegan, boolean vegetarian,
                      boolean glutenFree, int typeofItem, boolean available) {
        this.id = id;
        this.name = name;
        this.salePercentage = salePercentage;
        this.category = category;
        this.typeofItem = typeofItem;
        this.ingredients = ingredients;
        this.price = price;
        this.vegan = vegan;
        this.vegetarian = vegetarian;
        this.glutenFree = glutenFree;
        this.available = available;
    }

    //Constructor for combo menus
    public G3MenuItem(long id, String name, double price, int salePercentage, String category, String items, String additionalClause , int typeofItem, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.salePercentage = salePercentage;
        this.category = category;
        this.typeofItem = typeofItem;
        this.items = items;
        this.additionalClause = additionalClause;
    }

    public String getCategory() {
        return category;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getSalePercentage() {
        return salePercentage;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSalePercentage(int salePercentage) {
        this.salePercentage = salePercentage;
    }

    public int getTypeofItem() {
        return typeofItem;
    }

    public void setTypeofItem(int typeofItem) {
        this.typeofItem = typeofItem;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getAdditionalClause() {
        return additionalClause;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setAdditionalClause(String additionalClause) {
        this.additionalClause = additionalClause;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;

        if ( !(o instanceof G3MenuItem) ) return false;

        G3MenuItem that = (G3MenuItem) o;

        if(this.id == that.getId())
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
        dest.writeLong(this.id);
        dest.writeInt(this.salePercentage);
        dest.writeInt(this.typeofItem);
        dest.writeByte(this.available ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.price);
        dest.writeByte(this.vegan ? (byte) 1 : (byte) 0);
        dest.writeByte(this.vegetarian ? (byte) 1 : (byte) 0);
        dest.writeByte(this.glutenFree ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
        dest.writeString(this.category);
        dest.writeString(this.items);
        dest.writeString(this.additionalClause);
        dest.writeString(this.ingredients);
    }

    protected G3MenuItem(Parcel in) {
        this.id = in.readLong();
        this.salePercentage = in.readInt();
        this.typeofItem = in.readInt();
        this.available = in.readByte() != 0;
        this.price = in.readDouble();
        this.vegan = in.readByte() != 0;
        this.vegetarian = in.readByte() != 0;
        this.glutenFree = in.readByte() != 0;
        this.name = in.readString();
        this.category = in.readString();
        this.items = in.readString();
        this.additionalClause = in.readString();
        this.ingredients = in.readString();
    }

    public static final Parcelable.Creator<G3MenuItem> CREATOR = new Parcelable.Creator<G3MenuItem>() {
        @Override
        public G3MenuItem createFromParcel(Parcel source) {
            return new G3MenuItem(source);
        }

        @Override
        public G3MenuItem[] newArray(int size) {
            return new G3MenuItem[size];
        }
    };

}
