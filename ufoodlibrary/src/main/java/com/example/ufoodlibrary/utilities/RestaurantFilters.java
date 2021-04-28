package com.example.ufoodlibrary.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ufoodlibrary.objects.G3MenuItem;
import com.example.ufoodlibrary.objects.G3OpeningHoursObj;
import com.example.ufoodlibrary.objects.G3PaymentMethods;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.google.android.gms.maps.model.LatLng;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luigi on 27/04/2016.
 */
public class RestaurantFilters implements Parcelable {

    private final static double RADIUS_EARTH = 6371;

    // Distance filter - In meters (i.e. 500, 1000, 2000, 5000)
    private int radius;

    // Restaurant category filter - A list of int, one for each checked restaurant category on
    // filters dialog. Integers to be added in that list must corresponds to RestaurantCategory enum.
    private List<Integer>  restaurantCatFilter;

    // Dietary needs filter - true if checked
    boolean isVegetarianFilter;
    boolean isVeganFilter;
    boolean isGlutenFreeFilter;

    // Payment methods filter - true if checked
    private boolean acceptCreditCardFilter;
    private boolean acceptMoneyFilter;
    private boolean acceptBancomatFilter;
    private boolean acceptTicketFilter;

    // Budget filter - true if checked
    private boolean priceCatLowFilter;
    private boolean priceCatMedFilter;
    private boolean priceCatHighFilter;

    public RestaurantFilters() {
        this.radius = 500;
        this.restaurantCatFilter = new ArrayList<>();
        this.isVegetarianFilter = false;
        this.isVeganFilter = false;
        this.isGlutenFreeFilter = false;
        this.acceptCreditCardFilter = false;
        this.acceptMoneyFilter = false;
        this.acceptBancomatFilter = false;
        this.acceptTicketFilter = false;
        this.priceCatLowFilter = false;
        this.priceCatMedFilter = false;
        this.priceCatHighFilter = false;
    }

    public RestaurantFilters(RestaurantFilters filter) {
        this.radius = filter.getRadius();
        this.restaurantCatFilter = new ArrayList<>();
        this.restaurantCatFilter.addAll(filter.getRestaurantCatFilter());
        this.isVegetarianFilter = filter.getVegetarianFilter();
        this.isVeganFilter = filter.getVeganFilter();
        this.isGlutenFreeFilter = filter.getGlutenFreeFilter();
        this.acceptCreditCardFilter = filter.getAcceptCreditCardFilter();
        this.acceptMoneyFilter = filter.getAcceptMoneyFilter();
        this.acceptBancomatFilter = filter.getAcceptBancomatFilter();
        this.acceptTicketFilter = filter.getAcceptTicketFilter();
        this.priceCatLowFilter = filter.getPriceCatLowFilter();
        this.priceCatMedFilter = filter.getPriceCatMedFilter();
        this.priceCatHighFilter = filter.getPriceCatHighFilter();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public List<Integer> getRestaurantCatFilter() {
        return restaurantCatFilter;
    }

    public void setRestaurantCatFilter(List<Integer> restaurantCatFilter) {
        this.restaurantCatFilter = restaurantCatFilter;
    }

    public boolean getVegetarianFilter() {
        return isVegetarianFilter;
    }

    public void setVegetarianFilter(boolean vegetarianFilter) {
        isVegetarianFilter = vegetarianFilter;
    }

    public boolean getVeganFilter() {
        return isVeganFilter;
    }

    public void setVeganFilter(boolean veganFilter) {
        isVeganFilter = veganFilter;
    }

    public boolean getGlutenFreeFilter() {
        return isGlutenFreeFilter;
    }

    public void setGlutenFreeFilter(boolean glutenFreeFilter) {
        isGlutenFreeFilter = glutenFreeFilter;
    }

    public boolean getAcceptCreditCardFilter() {
        return acceptCreditCardFilter;
    }

    public void setAcceptCreditCardFilter(boolean acceptCreditCardFilter) {
        this.acceptCreditCardFilter = acceptCreditCardFilter;
    }

    public boolean getAcceptMoneyFilter() {
        return acceptMoneyFilter;
    }

    public void setAcceptMoneyFilter(boolean acceptMoneyFilter) {
        this.acceptMoneyFilter = acceptMoneyFilter;
    }

    public boolean getAcceptBancomatFilter() {
        return acceptBancomatFilter;
    }

    public void setAcceptBancomatFilter(boolean acceptBancomatFilter) {
        this.acceptBancomatFilter = acceptBancomatFilter;
    }

    public boolean getAcceptTicketFilter() {
        return acceptTicketFilter;
    }

    public void setAcceptTicketFilter(boolean acceptTicketFilter) {
        this.acceptTicketFilter = acceptTicketFilter;
    }

    public boolean getPriceCatHighFilter() {
        return priceCatHighFilter;
    }

    public void setPriceCatHighFilter(boolean priceCatHighFilter) {
        this.priceCatHighFilter = priceCatHighFilter;
    }

    public boolean getPriceCatLowFilter() {
        return priceCatLowFilter;
    }

    public void setPriceCatLowFilter(boolean priceCatLowFilter) {
        this.priceCatLowFilter = priceCatLowFilter;
    }

    public boolean getPriceCatMedFilter() {
        return priceCatMedFilter;
    }

    public void setPriceCatMedFilter(boolean priceCatMedFilter) {
        this.priceCatMedFilter = priceCatMedFilter;
    }

    public ArrayList<G3RestaurantObj> applyRadiusFilterToList(ArrayList<G3RestaurantObj> inputList, LatLng currentPos) {

        ArrayList<G3RestaurantObj> filteredList = new ArrayList<>();

        // Apply distance filter
        for (G3RestaurantObj r: inputList) {

            LatLng rPos = new LatLng(r.getAddressObj().getLatitude(), r.getAddressObj().getLongitude());
            double distance = calculateDistance(currentPos, rPos) * 1000;

            if(distance <= radius)
                filteredList.add(r);

        }

        return filteredList;

    }

    public boolean applyFilters (G3RestaurantObj restaurant) {

        boolean isOpen = false;

        DateTime now = new DateTime();
        String dayOfTheWeek = G3OpeningHoursObj.Day.getDayFromInt(
                now.getDayOfWeek() - 1 ).getString();

        // Check if the restaurant is open
        if (restaurant.getOpeningHours().containsKey(dayOfTheWeek + JsonKey.LUNCHTAG)) {

            int from = restaurant.getOpeningHours().get(dayOfTheWeek + JsonKey.LUNCHTAG).getFrom();
            int to = restaurant.getOpeningHours().get(dayOfTheWeek + JsonKey.LUNCHTAG).getTo();

            String f = String.format("%04d", from);
            String t = String.format("%04d", to);

            Integer fh = Integer.valueOf(f.substring(0, 2));
            Integer fm = Integer.valueOf(f.substring(2));

            Integer th = Integer.valueOf(t.substring(0, 2));
            Integer tm = Integer.valueOf(t.substring(2));

            DateTime open = now.withHourOfDay(fh).withMinuteOfHour(fm);
            DateTime close = now.withHourOfDay(th).withMinuteOfHour(tm);

            if (now.isBefore(close)) isOpen = true;

        }

        if (restaurant.getOpeningHours().containsKey(dayOfTheWeek + JsonKey.DINNERTAG)) {

            int from = restaurant.getOpeningHours().get(dayOfTheWeek + JsonKey.DINNERTAG).getFrom();
            int to = restaurant.getOpeningHours().get(dayOfTheWeek + JsonKey.DINNERTAG).getTo();

            String f = String.format("%04d", from);
            String t = String.format("%04d", to);

            Integer fh = Integer.valueOf(f.substring(0, 2));
            Integer fm = Integer.valueOf(f.substring(2));

            Integer th = Integer.valueOf(t.substring(0, 2));
            Integer tm = Integer.valueOf(t.substring(2));

            DateTime open = now.withHourOfDay(fh).withMinuteOfHour(fm);
            DateTime close = now.withHourOfDay(th).withMinuteOfHour(tm);

            if (now.isBefore(close)) isOpen = true;

        }

        // If the restaurant is open apply other filters if any, return false otherwise
        if (!isOpen) return false;

        // Restaurant category filter
        if (restaurantCatFilter.size() != 0) {

            if ( !restaurantCatFilter.contains(restaurant.getCategoryAsEnum().getNumber()) )
                return false;

        }

        // Dietary needs filter
        if (isGlutenFreeFilter || isVeganFilter || isVegetarianFilter) {

            boolean isGlutenFree = false;
            boolean isVegan = false;
            boolean isVegetarian = false;

            try {
                for (G3MenuItem it : restaurant.getMenu().getMenuItems()) {

                    if (it.isAvailable()) {
                        if (it.isGlutenFree())
                            isGlutenFree = true;
                        if (it.isVegan())
                            isVegan = true;
                        if (it.isVegetarian())
                            isVegetarian = true;
                    }

                }
            } catch (NullPointerException e) {
                return false;
            }

//            if ((!isGlutenFreeFilter || (isGlutenFreeFilter && isGlutenFree)) &&
//                    (!isVeganFilter || (isVeganFilter && isVegan)) &&
//                    (!isVegetarianFilter || (isVegetarianFilter && isVegetarian)))
//                result = true;
//            else
            if ( (isGlutenFreeFilter && !isGlutenFree) ||
                    (isVeganFilter && !isVegan) ||
                    (isVegetarianFilter && !isVegetarian) )
                return false;

        }

        // Payment methods filter
        if (acceptMoneyFilter || acceptBancomatFilter || acceptCreditCardFilter || acceptTicketFilter) {

            G3PaymentMethods rPay = restaurant.getPaymentMethods();

//            if ((!acceptMoneyFilter || (acceptMoneyFilter && rPay.isMoney())) &&
//                    (!acceptBancomatFilter || (acceptBancomatFilter && rPay.isBancomat())) &&
//                    (!acceptCreditCardFilter || (acceptCreditCardFilter && rPay.isCreditCard())) &&
//                    (!acceptTicketFilter || (acceptTicketFilter && rPay.isTicket())))
//                result = true;
//            else
            if ( (acceptMoneyFilter && !rPay.isMoney()) ||
                    (acceptBancomatFilter && !rPay.isBancomat()) ||
                    (acceptCreditCardFilter && !rPay.isCreditCard()) ||
                    (acceptTicketFilter && !rPay.isTicket()) )
                return false;

        }

        if (priceCatLowFilter || priceCatMedFilter || priceCatHighFilter) {

            if ((priceCatLowFilter && restaurant.getPriceRating() == 1) ||
                    (priceCatMedFilter && restaurant.getPriceRating() == 2) ||
                    (priceCatHighFilter && restaurant.getPriceRating() == 3))
                return true;
            else
                return false;

        }

        return true;

    }

    public ArrayList<G3RestaurantObj> applyFiltersToList(ArrayList<G3RestaurantObj> inputList) {

        ArrayList<G3RestaurantObj> tempList = new ArrayList<>();
        ArrayList<G3RestaurantObj> filteredList = new ArrayList<>();

        DateTime now = new DateTime();
        String dayOfTheWeek = G3OpeningHoursObj.Day.getDayFromInt(
                now.getDayOfWeek() - 1 ).getString();

        // Filtering out closed restaurants
        for (G3RestaurantObj r: inputList) {

            if (r.getOpeningHours().containsKey(dayOfTheWeek + JsonKey.LUNCHTAG)) {

                int from = r.getOpeningHours().get(dayOfTheWeek + JsonKey.LUNCHTAG).getFrom();
                int to = r.getOpeningHours().get(dayOfTheWeek + JsonKey.LUNCHTAG).getTo();

                String f = String.format("%04d", from);
                String t = String.format("%04d", to);

                Integer fh = Integer.valueOf(f.substring(0, 2));
                Integer fm = Integer.valueOf(f.substring(2));

                Integer th = Integer.valueOf(t.substring(0, 2));
                Integer tm = Integer.valueOf(t.substring(2));

                DateTime open = now.withHourOfDay(fh).withMinuteOfHour(fm);
                DateTime close = now.withHourOfDay(th).withMinuteOfHour(tm);

                if (now.isBefore(close)) {
                    filteredList.add(r);
                    continue;
                }

            }

            if (r.getOpeningHours().containsKey(dayOfTheWeek + JsonKey.DINNERTAG)) {

                int from = r.getOpeningHours().get(dayOfTheWeek + JsonKey.DINNERTAG).getFrom();
                int to = r.getOpeningHours().get(dayOfTheWeek + JsonKey.DINNERTAG).getTo();

                String f = String.format("%04d", from);
                String t = String.format("%04d", to);

                Integer fh = Integer.valueOf(f.substring(0, 2));
                Integer fm = Integer.valueOf(f.substring(2));

                Integer th = Integer.valueOf(t.substring(0, 2));
                Integer tm = Integer.valueOf(t.substring(2));

                DateTime open = now.withHourOfDay(fh).withMinuteOfHour(fm);
                DateTime close = now.withHourOfDay(th).withMinuteOfHour(tm);

                if (now.isBefore(close))
                    filteredList.add(r);

            }

        }

        // Apply other filters if any

        // Restaurant category filter
        if (restaurantCatFilter.size() != 0) {

            swapLists(filteredList, tempList);

            for (G3RestaurantObj r: tempList) {

                if ( restaurantCatFilter.contains(r.getCategoryAsEnum().getNumber()) )
                    filteredList.add(r);

            }

        }

        // Dietary needs filter
        if (isGlutenFreeFilter || isVeganFilter || isVegetarianFilter) {

            swapLists(filteredList, tempList);

            for (G3RestaurantObj r: tempList) {

                boolean isGlutenFree = false;
                boolean isVegan = false;
                boolean isVegetarian = false;

                try {
                    for (G3MenuItem it : r.getMenu().getMenuItems()) {

                        if (it.isAvailable()) {
                            if (it.isGlutenFree())
                                isGlutenFree = true;
                            if (it.isVegan())
                                isVegan = true;
                            if (it.isVegetarian())
                                isVegetarian = true;
                        }

                    }
                } catch (NullPointerException e) {
                    continue;
                }

                if ((!isGlutenFreeFilter || (isGlutenFreeFilter && isGlutenFree)) &&
                        (!isVeganFilter || (isVeganFilter && isVegan)) &&
                        (!isVegetarianFilter || (isVegetarianFilter && isVegetarian)))
                    filteredList.add(r);

            }

        }

        // Payment methods filter
        if (acceptMoneyFilter || acceptBancomatFilter || acceptCreditCardFilter || acceptTicketFilter) {

            swapLists(filteredList, tempList);

            for (G3RestaurantObj r: tempList) {

                G3PaymentMethods rPay = r.getPaymentMethods();

                if ((!acceptMoneyFilter || (acceptMoneyFilter && rPay.isMoney())) &&
                    (!acceptBancomatFilter || (acceptBancomatFilter && rPay.isBancomat())) &&
                        (!acceptCreditCardFilter || (acceptCreditCardFilter && rPay.isCreditCard())) &&
                        (!acceptTicketFilter || (acceptTicketFilter && rPay.isTicket())))
                    filteredList.add(r);

            }

        }

        if (priceCatLowFilter || priceCatMedFilter || priceCatHighFilter) {

            swapLists(filteredList, tempList);

            for (G3RestaurantObj r: tempList) {

                if ((priceCatLowFilter && r.getPriceRating() == 1) ||
                        (priceCatMedFilter && r.getPriceRating() == 2) ||
                        (priceCatHighFilter && r.getPriceRating() == 3)) {

                    filteredList.add(r);

                }

            }

        }

        return filteredList;

    }

    private void swapLists(ArrayList<G3RestaurantObj> filteredList, ArrayList<G3RestaurantObj> tempList) {

        tempList.clear();
        tempList.addAll(filteredList);
        filteredList.clear();

    }

    public static double calculateDistance(LatLng a, LatLng b) {

        double deltaLambda = Math.abs(a.longitude - b.longitude) * Math.PI/180;

        double deltaSigma = Math.acos(
                Math.sin(a.latitude*Math.PI/180) * Math.sin(b.latitude*Math.PI/180) +
                Math.cos(a.latitude*Math.PI/180) * Math.cos(b.latitude*Math.PI/180) * Math.cos(deltaLambda)
        );

        return (RADIUS_EARTH * deltaSigma);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.radius);
        dest.writeList(this.restaurantCatFilter);
        dest.writeByte(isVegetarianFilter ? (byte) 1 : (byte) 0);
        dest.writeByte(isVeganFilter ? (byte) 1 : (byte) 0);
        dest.writeByte(isGlutenFreeFilter ? (byte) 1 : (byte) 0);
        dest.writeByte(acceptCreditCardFilter ? (byte) 1 : (byte) 0);
        dest.writeByte(acceptMoneyFilter ? (byte) 1 : (byte) 0);
        dest.writeByte(acceptBancomatFilter ? (byte) 1 : (byte) 0);
        dest.writeByte(acceptTicketFilter ? (byte) 1 : (byte) 0);
        dest.writeByte(priceCatLowFilter ? (byte) 1 : (byte) 0);
        dest.writeByte(priceCatMedFilter ? (byte) 1 : (byte) 0);
        dest.writeByte(priceCatHighFilter ? (byte) 1 : (byte) 0);
    }

    protected RestaurantFilters(Parcel in) {
        this.radius = in.readInt();
        this.restaurantCatFilter = new ArrayList<Integer>();
        in.readList(this.restaurantCatFilter, Integer.class.getClassLoader());
        this.isVegetarianFilter = in.readByte() != 0;
        this.isVeganFilter = in.readByte() != 0;
        this.isGlutenFreeFilter = in.readByte() != 0;
        this.acceptCreditCardFilter = in.readByte() != 0;
        this.acceptMoneyFilter = in.readByte() != 0;
        this.acceptBancomatFilter = in.readByte() != 0;
        this.acceptTicketFilter = in.readByte() != 0;
        this.priceCatLowFilter = in.readByte() != 0;
        this.priceCatMedFilter = in.readByte() != 0;
        this.priceCatHighFilter = in.readByte() != 0;
    }

    public static final Creator<RestaurantFilters> CREATOR = new Creator<RestaurantFilters>() {
        @Override
        public RestaurantFilters createFromParcel(Parcel source) {
            return new RestaurantFilters(source);
        }

        @Override
        public RestaurantFilters[] newArray(int size) {
            return new RestaurantFilters[size];
        }
    };

}
