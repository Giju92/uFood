package com.example.ufoodlibrary.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Luigi on 06/05/2016.
 */
public class G3ReviewObj implements Parcelable {

    private String id;
    private String restaurantId;
    private String orderId;
    private String userid;
    private Date date;
    private double foodRating;
    private double punctualityRating;
    private double serviceRating;
    private double overallRating;
    private String reviewText;
    private String reply;

    public G3ReviewObj(){

    }

    public G3ReviewObj(String restaurantId,String userid, String orderId, Date date, double foodRating, double punctualityRating, double serviceRating, String reviewText) {
        this.restaurantId = restaurantId;
        this.orderId = orderId;
        this.userid = userid;
        this.date = date;
        this.foodRating = foodRating;
        this.punctualityRating = punctualityRating;
        this.serviceRating = serviceRating;
        this.reviewText = reviewText;
        this.overallRating = (foodRating + serviceRating + serviceRating) / 3;
        this.reply = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return userid;
    }

    public void setUsername(String userid) {
        this.userid = userid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getFoodRating() {
        return foodRating;
    }

    public void setFoodRating(double foodRating) {
        this.foodRating = foodRating;
    }

    public double getPunctualityRating() {
        return punctualityRating;
    }

    public void setPunctualityRating(double punctualityRating) {
        this.punctualityRating = punctualityRating;
    }

    public double getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(double serviceRating) {
        this.serviceRating = serviceRating;
    }

    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public G3OrderObj getOrderFromId(Collection<G3OrderObj> orders) {

        G3OrderObj bRet = null;

        for (G3OrderObj o: orders) {
            if (o.getReviewId() == this.id) {
                bRet = o;
                break;
            }
        }

        return bRet;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.restaurantId);
        dest.writeString(this.orderId);
        dest.writeString(this.userid);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeDouble(this.foodRating);
        dest.writeDouble(this.punctualityRating);
        dest.writeDouble(this.serviceRating);
        dest.writeDouble(this.overallRating);
        dest.writeString(this.reviewText);
        dest.writeString(this.reply);
    }

    protected G3ReviewObj(Parcel in) {
        this.id = in.readString();
        this.restaurantId = in.readString();
        this.orderId = in.readString();
        this.userid = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.foodRating = in.readDouble();
        this.punctualityRating = in.readDouble();
        this.serviceRating = in.readDouble();
        this.overallRating = in.readDouble();
        this.reviewText = in.readString();
        this.reply = in.readString();
    }

    public static final Creator<G3ReviewObj> CREATOR = new Creator<G3ReviewObj>() {
        @Override
        public G3ReviewObj createFromParcel(Parcel source) {
            return new G3ReviewObj(source);
        }

        @Override
        public G3ReviewObj[] newArray(int size) {
            return new G3ReviewObj[size];
        }
    };

}
