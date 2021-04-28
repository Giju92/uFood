package com.example.ufoodlibrary.objects;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.utilities.G3Application;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mattia on 08/04/2016.
 */
public class G3OrderObj implements Parcelable {

    private String orderid;
    private String restaurantId;
    private String reviewId; // -1: Order not rated/reviewed
    private String userid;
    private List<G3OrderItem> orderItems;
    private double totalOrderPrice;
    private String additionalNotes;
    private Date date;
    private int orderState; // 0: Pending, 1: Completed, 2: Cancelled
    private boolean visualized;
    private boolean userVisualized;
    private int seats;
    private long timestamp;

    public G3OrderObj(){

    }

    public G3OrderObj( String restaurantId, String user, String additionalNotes, Date date, long timestamp) {
        this.orderid = null;
        this.restaurantId = restaurantId;
        this.reviewId = null;
        this.userid = user;
        this.orderItems = new ArrayList<>();
        this.additionalNotes = additionalNotes;
        this.date = date;
        this.totalOrderPrice = 0;
        this.orderState = 0;
        this.visualized = false;
        this.userVisualized = true;
        this.seats = 0;
        this.timestamp = timestamp;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getReviewId() { return reviewId; }

    public void setReviewId(String reviewId) { this.reviewId = reviewId; }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public boolean isVisualized() {
        return visualized;
    }

    public void setVisualized(boolean visualized) {
        this.visualized = visualized;
    }

    public boolean isUserVisualized() {
        return userVisualized;
    }

    public void setUserVisualized(boolean userVisualized) {
        this.userVisualized = userVisualized;
    }

    public List<G3OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<G3OrderItem> orderItems) {
        this.orderItems = orderItems;
        if(orderItems != null){
            totalOrderPrice = 0;
            for(G3OrderItem temp : orderItems){
                totalOrderPrice += temp.getTotalPrice();
            }
        }

    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void refreshOrder(){
        totalOrderPrice = 0;
        for(G3OrderItem temp : orderItems){
            totalOrderPrice += temp.getTotalPrice();
        }
    }

    public void addOrderItem(G3OrderItem item){

        for(G3OrderItem order:orderItems ){
            if(order.getMenuItem() != null) {
                if (order.getMenuItem().getName().equalsIgnoreCase(item.getMenuItem().getName())) {
                    order.setQuantity(order.getQuantity() + item.getQuantity());
                    this.totalOrderPrice += item.getTotalPrice();
                    return;
                }
            }
        }
        this.orderItems.add(item);
        this.totalOrderPrice += item.getTotalPrice();

    }

    public void removeOrderItem(int pos){
        this.orderItems.remove(pos);
        if(orderItems != null){
            totalOrderPrice = 0;
            for(G3OrderItem temp : orderItems){

                totalOrderPrice += temp.getTotalPrice();
            }
        }
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;

        if ( !(o instanceof G3OrderObj) ) return false;

        G3OrderObj that = (G3OrderObj) o;

        if(this.orderid == that.getOrderid())
            return true;
        else
            return false;
    }

    public void delete(){
        this.orderItems.clear();
        this.totalOrderPrice= 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderid);
        dest.writeString(this.restaurantId);
        dest.writeString(this.reviewId);
        dest.writeString(this.userid);
        dest.writeTypedList(this.orderItems);
        dest.writeDouble(this.totalOrderPrice);
        dest.writeString(this.additionalNotes);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeInt(this.orderState);
        dest.writeByte(this.visualized ? (byte) 1 : (byte) 0);
        dest.writeByte(this.userVisualized ? (byte) 1 : (byte) 0);
        dest.writeInt(this.seats);
        dest.writeLong(this.timestamp);
    }

    protected G3OrderObj(Parcel in) {
        this.orderid = in.readString();
        this.restaurantId = in.readString();
        this.reviewId = in.readString();
        this.userid = in.readString();
        this.orderItems = in.createTypedArrayList(G3OrderItem.CREATOR);
        this.totalOrderPrice = in.readDouble();
        this.additionalNotes = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.orderState = in.readInt();
        this.visualized = in.readByte() != 0;
        this.userVisualized = in.readByte() != 0;
        this.seats = in.readInt();
        this.timestamp = in.readLong();
    }

    public static final Creator<G3OrderObj> CREATOR = new Creator<G3OrderObj>() {
        @Override
        public G3OrderObj createFromParcel(Parcel source) {
            return new G3OrderObj(source);
        }

        @Override
        public G3OrderObj[] newArray(int size) {
            return new G3OrderObj[size];
        }
    };

    @Exclude
    public String getOrderasString(){

        switch (getOrderState()) {
            case 0:
                return G3Application.getAppContext().getResources().getString(R.string.pending_status);
            case 1:
                return G3Application.getAppContext().getResources().getString(R.string.done_status);
            case 2:
                return G3Application.getAppContext().getResources().getString(R.string.rejected_status);

            default:
                return "";
        }
    }

}
