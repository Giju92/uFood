package com.example.ufoodlibrary.objects;



import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alfonso on 01-Apr-16.
 */
public class G3OrderItem implements Parcelable {
    private long id;
    private G3MenuItem menuItem;
    private int quantity;
    private double totalPrice;

    public G3OrderItem(){

    }

    public G3OrderItem(long id, G3MenuItem menuItem, int quantity) {
        this.id = id;
        this.menuItem = menuItem;
        this.quantity = quantity;
        if(menuItem.getPrice()>0){
            this.totalPrice = menuItem.getPrice()*quantity-menuItem.getPrice()*menuItem.getSalePercentage()*quantity/100;
        } else {
            this.totalPrice = menuItem.getPrice()*quantity;
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public G3MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(G3MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if(menuItem != null) {
            if(menuItem.getSalePercentage()>0){
                this.totalPrice = quantity * menuItem.getPrice() - quantity * menuItem.getPrice() * menuItem.getSalePercentage() /100;
            } else {
                this.totalPrice = quantity * menuItem.getPrice();
            }
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeParcelable(this.menuItem, flags);
        dest.writeInt(this.quantity);
        dest.writeDouble(this.totalPrice);
    }

    protected G3OrderItem(Parcel in) {
        this.id = in.readLong();
        this.menuItem = in.readParcelable(G3MenuItem.class.getClassLoader());
        this.quantity = in.readInt();
        this.totalPrice = in.readDouble();
    }

    public static final Parcelable.Creator<G3OrderItem> CREATOR = new Parcelable.Creator<G3OrderItem>() {
        @Override
        public G3OrderItem createFromParcel(Parcel source) {
            return new G3OrderItem(source);
        }

        @Override
        public G3OrderItem[] newArray(int size) {
            return new G3OrderItem[size];
        }
    };

}
