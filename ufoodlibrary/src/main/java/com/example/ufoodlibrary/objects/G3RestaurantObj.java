package com.example.ufoodlibrary.objects;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.utilities.G3Application;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.maps.android.clustering.ClusterItem;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Alfonso on 01-Apr-16.
 */
public class G3RestaurantObj implements ClusterItem, Parcelable {

    private String id;
    private String name;
    private String photoPath;
    protected restaurantCategory category;
    private G3AddressObj addressObj;
    private String phoneNumber;
    private String mobileNumber;
    private String email;
    private String website;
    private HashMap<String,G3OpeningHoursObj> openingHours;
    private int serviceTime;
    private boolean allowTableBooking;
    private int seats;
    private G3DailyMenu mDailymenu;
    private G3MenuObj menu; //
    private G3PaymentMethods paymentMethods; //
    private int numOfReviews;
    private double reviewsAvgScore;
    private int priceRating;

    private static Resources res = G3Application.getAppContext().getResources();

    private static final String CATEGORY_ASIAN = res.getString(R.string.asian);
    private static final String CATEGORY_AFRICAN = res.getString(R.string.african);
    private static final String CATEGORY_BAR = res.getString(R.string.bar);
    private static final String CATEGORY_CHINESE = res.getString(R.string.chinese);
    private static final String CATEGORY_REGIONAL_KITCHEN = res.getString(R.string.regional);
    private static final String CATEGORY_MEAT_RESTAURANT = res.getString(R.string.meat);
    private static final String CATEGORY_SEA_FOOD = res.getString(R.string.sea);
    private static final String CATEGORY_FAST_FOOD = res.getString(R.string.fast);
    private static final String CATEGORY_FUSION = res.getString(R.string.fusion);
    private static final String CATEGORY_JAPANESE = res.getString(R.string.japanese);
    private static final String CATEGORY_GREEK = res.getString(R.string.greek);
    private static final String CATEGORY_HAMBURGER = res.getString(R.string.hamburger);
    private static final String CATEGORY_INDIAN = res.getString(R.string.indian);
    private static final String CATEGORY_ITALIAN = res.getString(R.string.italian);
    private static final String CATEGORY_KEBAB = res.getString(R.string.kebab);
    private static final String CATEGORY_MEDITERRANEAN = res.getString(R.string.mediterranean);
    private static final String CATEGORY_ORIENTAL = res.getString(R.string.oriental);
    private static final String CATEGORY_SANDWICH = res.getString(R.string.sandwich);
    private static final String CATEGORY_PIADINERIA = res.getString(R.string.piadineria);
    private static final String CATEGORY_PIZZERIA = res.getString(R.string.pizzeria);
    private static final String CATEGORY_PIEDMONT = res.getString(R.string.piedmont);
    private static final String CATEGORY_FRY_SHOP = res.getString(R.string.fry);
    private static final String CATEGORY_SUD_AMERICAN = res.getString(R.string.sudamerican);
    private static final String CATEGORY_VEGETARIAN = res.getString(R.string.vegetarian_shop);

    @Override
    @Exclude
    public LatLng getPosition() {
        return new LatLng(addressObj.getLatitude(), addressObj.getLongitude());
    }

    public enum restaurantCategory {

        ASIAN(0,CATEGORY_ASIAN),
        AFRICAN(1,CATEGORY_AFRICAN),
        BAR(2,CATEGORY_BAR),
        CHINESE(3,CATEGORY_CHINESE),
        MEAT_RESTAURANT(4,CATEGORY_MEAT_RESTAURANT),
        SEA_FOOD(5,CATEGORY_SEA_FOOD),
        FAST_FOOD(6,CATEGORY_FAST_FOOD),
        FUSION(7,CATEGORY_FUSION),
        JAPANESE(8,CATEGORY_JAPANESE),
        GREEK(9,CATEGORY_GREEK),
        HAMBURGER(10,CATEGORY_HAMBURGER),
        INDIAN(11,CATEGORY_INDIAN),
        ITALIAN(12,CATEGORY_ITALIAN),
        KEBAB(13,CATEGORY_KEBAB),
        MEDITERRANEAN(14,CATEGORY_MEDITERRANEAN),
        ORIENTAL(15,CATEGORY_ORIENTAL),
        SANDWICH(16,CATEGORY_SANDWICH),
        PIADINERIA(17,CATEGORY_PIADINERIA),
        PIZZERIA(18,CATEGORY_PIZZERIA),
        PIEDMONT(19,CATEGORY_PIEDMONT),
        FRY_SHOP(20,CATEGORY_FRY_SHOP),
        SUD_AMERICAN(21,CATEGORY_SUD_AMERICAN),
        VEGETARIAN(22,CATEGORY_VEGETARIAN),
        REGIONAL_KITCHEN(23,CATEGORY_REGIONAL_KITCHEN);

        private String str;
        private int num;

        restaurantCategory(){

        }

        restaurantCategory(int num,String str) {
            this.str = new String(str);
            this.num = num;
        }

        public String getString() {

            return new String(this.str);
        }

        public int getNumber() {

            return this.num;
        }



        public static restaurantCategory getRestaurantCategoryFromString(String str) {

            restaurantCategory rVal = null;

            if (str != null) {
                for (restaurantCategory ie : restaurantCategory.values()) {
                    if (ie.getString().compareTo(str) == 0) {
                        rVal = ie;
                        break;
                    }
                }
            }

            return rVal;
        }

        public static restaurantCategory getRestaurantCategoryFromInt(int n) {

            restaurantCategory rVal = null;

            if (n >= 0) {
                for (restaurantCategory ie : restaurantCategory.values()) {
                    if (ie.getNumber() == n) {
                        rVal = ie;
                        break;
                    }
                }
            }

            return rVal;
        }
    }

    public  G3RestaurantObj(){

    }

    public G3RestaurantObj( String name, String photoPath,
                           restaurantCategory category, G3AddressObj addressObj, String phoneNumber,
                           String mobileNumber, String email, String website,
                           HashMap<String,G3OpeningHoursObj> openingHours, boolean allowTableBooking,
                           int seats, int serviceTime, G3PaymentMethods paymentMethods) {
        this.name = name;
        this.photoPath = photoPath;
        this.category = category;
        this.addressObj = addressObj;
        this.phoneNumber = phoneNumber;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.website = website;
        this.openingHours = openingHours;
        this.serviceTime = serviceTime;
        this.allowTableBooking = allowTableBooking;
        this.seats = seats;
        this.paymentMethods = paymentMethods;
        this.menu = null;
        this.numOfReviews = 0;
        this.reviewsAvgScore = 0;
        this.priceRating = 0;
        this.mDailymenu = null;

    }

    public G3DailyMenu getmDailymenu() {
        return mDailymenu;
    }

    public void setmDailymenu(G3DailyMenu menu){
        this.mDailymenu = menu;
    }

    public void removeDailyMenu( ){
        this.mDailymenu = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    @Exclude
    public restaurantCategory getCategoryAsEnum() {
        return category;
    }

    public String getCategory(){
        if (category == null){
            return null;
        } else {
            return category.name();
        }
    }

    public void setCategory(String restaurantCategorys){
        if (restaurantCategorys == null){
            category = null;
        } else {
            this.category = restaurantCategory.valueOf(restaurantCategorys);
        }
    }

    public void setCategoryAsEnum(restaurantCategory category) {
        this.category = category;
    }

    public G3AddressObj getAddressObj() {
        return addressObj;
    }

    public void setAddressObj(G3AddressObj addressObj) {
        this.addressObj = addressObj;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public HashMap<String,G3OpeningHoursObj> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(HashMap<String,G3OpeningHoursObj> openingHours) {
        this.openingHours = openingHours;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public boolean isAllowTableBooking() {
        return allowTableBooking;
    }

    public void setAllowTableBooking(boolean allowTableBooking) {
        this.allowTableBooking = allowTableBooking;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public G3MenuObj getMenu() {
        return menu;
    }

    public void setMenu(G3MenuObj menu) {
        this.menu = menu;
    }

    public G3PaymentMethods getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(G3PaymentMethods paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public int getNumOfReviews() {
        return numOfReviews;
    }

    public void setNumOfReviews(int numOfReviews) {
        this.numOfReviews = numOfReviews;
    }

    public double getReviewsAvgScore() {
        return reviewsAvgScore;
    }

    public void setReviewsAvgScore(double reviewsAvgScore) {
        this.reviewsAvgScore = reviewsAvgScore;
    }

    public int getPriceRating() {
        return priceRating;
    }

    public void setPriceRating(int priceRating) {
        this.priceRating = priceRating;
    }

    public void updateReviewsStats(double newReviewScore) {
        reviewsAvgScore = ((reviewsAvgScore * numOfReviews) + newReviewScore) / (numOfReviews + 1);
        numOfReviews++;
    }

    @Exclude
    public String getPriceRatingEuro() {

        if (priceRating==1)
            return "€";
        else if(priceRating==2)
            return "€€";
        else if(priceRating==3)
            return "€€€";
        return null;

    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;

        if ( !(o instanceof G3RestaurantObj) ) return false;

        G3RestaurantObj that = (G3RestaurantObj) o;

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
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.photoPath);
        dest.writeInt(this.category == null ? -1 : this.category.ordinal());
        dest.writeParcelable(this.addressObj, flags);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.mobileNumber);
        dest.writeString(this.email);
        dest.writeString(this.website);
        dest.writeInt(this.openingHours.size());
        for (Map.Entry<String, G3OpeningHoursObj> entry : this.openingHours.entrySet()) {
            dest.writeValue(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeInt(this.serviceTime);
        dest.writeByte(this.allowTableBooking ? (byte) 1 : (byte) 0);
        dest.writeInt(this.seats);
        dest.writeParcelable(this.menu, flags);
        dest.writeParcelable(this.paymentMethods, flags);
        dest.writeInt(this.numOfReviews);
        dest.writeDouble(this.reviewsAvgScore);
        dest.writeInt(this.priceRating);
        dest.writeParcelable(this.mDailymenu, flags);
    }

    protected G3RestaurantObj(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.photoPath = in.readString();
        int tmpCategory = in.readInt();
        this.category = tmpCategory == -1 ? null : restaurantCategory.values()[tmpCategory];
        this.addressObj = in.readParcelable(G3AddressObj.class.getClassLoader());
        this.phoneNumber = in.readString();
        this.mobileNumber = in.readString();
        this.email = in.readString();
        this.website = in.readString();
        int size = in.readInt();
        this.openingHours = new HashMap<String, G3OpeningHoursObj>(size);
        for (int i = 0; i < size; i++) {
            String key = (String) in.readValue(String.class.getClassLoader());
            G3OpeningHoursObj value = in.readParcelable(G3OpeningHoursObj.class.getClassLoader());
            this.openingHours.put(key, value);
        }
        this.serviceTime = in.readInt();
        this.allowTableBooking = in.readByte() != 0;
        this.seats = in.readInt();
        this.menu = in.readParcelable(G3MenuObj.class.getClassLoader());
        this.paymentMethods = in.readParcelable(G3PaymentMethods.class.getClassLoader());
        this.numOfReviews = in.readInt();
        this.reviewsAvgScore = in.readDouble();
        this.priceRating = in.readInt();
        this.mDailymenu = in.readParcelable(G3DailyMenu.class.getClassLoader());
    }

    public static final Parcelable.Creator<G3RestaurantObj> CREATOR = new Parcelable.Creator<G3RestaurantObj>() {
        @Override
        public G3RestaurantObj createFromParcel(Parcel source) {
            return new G3RestaurantObj(source);
        }

        @Override
        public G3RestaurantObj[] newArray(int size) {
            return new G3RestaurantObj[size];
        }
    };

}
