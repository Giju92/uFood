package com.example.ufoodlibrary.objects;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.regex.Pattern;

/**
 * Created by Alfonso on 04-Apr-16.
 */
public class G3AddressObj implements Parcelable {

    private String long_address;
    private String city;
    private String route;
    private int civic;

    private double latitude;
    private double longitude;

    public G3AddressObj(){

    }

    public G3AddressObj(String long_address, double latitude, double longitude) {
        this.long_address = long_address;
        this.latitude = latitude;
        this.longitude = longitude;

        String[] st_count = long_address.split(",\\s*");
        int cnt = st_count.length;

        if(cnt==3){

            // set the tokenizer for the long string
            String[] st = long_address.split(",\\s*");

            // get the address
            route = st[0];

            // check is the city is not unique in this token
            String[] st_city = st[1].split("\\s");
            if (st_city.length == 1) {
                city = st_city[0];
            } else {
                int i = 0;
                if (isNumeric(st_city[0])) {
                    i = 1;
                }
                city = "";
                while (i < st_city.length-1) {
                    city = city.concat(st_city[i]).concat(" ");
                    i++;
                }
                if (st_city[i].length() != 2 || !isAllUpperCase(st_city[i]))
                    city = city.concat(st_city[i]);
                else
                    city = city.trim();
            }

            // default civic
            civic = 0;
        }
        else{ // cnt == 4
            // set the tokenizer for the long string
            String[] st = long_address.split(",\\s*");

            // get the address
            route = st[0];

            // get civic
            civic = Integer.valueOf(st[1]);

            // check is the city is not unique in this token
            String[] st_city = st[2].split("\\s");
            if (st_city.length == 1) {
                city = st_city[0];
            } else {
                int i = 0;
                if (isNumeric(st_city[0])) {
                    i = 1;
                }
                city = "";
                while (i < st_city.length-1) {
                    city = city.concat(st_city[i]).concat(" ");
                    i++;
                }
                if (st_city[i].length() != 2 || !isAllUpperCase(st_city[i]))
                    city = city.concat(st_city[i]);
                else
                    city = city.trim();
            }
        }

    }

    private static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    private static boolean isAllUpperCase(String str) {
        Pattern p = Pattern.compile("[A-Z]*");
        return p.matcher(str).matches();
    }

    public String getLong_address() {
        return long_address;
    }

    public void setLong_address(String long_address) {
        this.long_address = long_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getCivic() {
        return civic;
    }

    public void setCivic(int civic) {
        this.civic = civic;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Exclude
    public String getShortAddress(){
        if (this.civic==0)
            return this.route;
        else
            return this.route+", "+this.civic;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.long_address);
        dest.writeString(this.city);
        dest.writeString(this.route);
        dest.writeInt(this.civic);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected G3AddressObj(Parcel in) {
        this.long_address = in.readString();
        this.city = in.readString();
        this.route = in.readString();
        this.civic = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<G3AddressObj> CREATOR = new Parcelable.Creator<G3AddressObj>() {
        @Override
        public G3AddressObj createFromParcel(Parcel source) {
            return new G3AddressObj(source);
        }

        @Override
        public G3AddressObj[] newArray(int size) {
            return new G3AddressObj[size];
        }
    };

}
