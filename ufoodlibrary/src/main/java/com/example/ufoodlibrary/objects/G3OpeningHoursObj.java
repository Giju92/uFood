package com.example.ufoodlibrary.objects;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Mattia on 06/04/2016.
 */
public class G3OpeningHoursObj implements Parcelable {

    private Day day;
    private String mKey;
    private int from;
    private int to;
    private boolean isAllDay = false;

    public static final String MONDAY_LUNCH = "monday_lunch";
    public static final String MONDAY_DINNER = "monday_dinner";
    public static final String TUESDAY_LUNCH = "tuesday_lunch";
    public static final String TUESDAY_DINNER = "tuesday_dinner";
    public static final String WEDNESDAY_LUNCH = "wednesday_lunch";
    public static final String WEDNESDAY_DINNER = "wednesday_dinner";
    public static final String THURSDAY_LUNCH = "thursday_lunch";
    public static final String THURSDAY_DINNER = "thursday_dinner";
    public static final String FRIDAY_LUNCH = "friday_lunch";
    public static final String FRIDAY_DINNER = "friday_dinner";
    public static final String SATURDAY_LUNCH = "saturday_lunch";
    public static final String SATURDAY_DINNER = "saturday_dinner";
    public static final String SUNDAY_LUNCH = "sunday_lunch";
    public static final String SUNDAY_DINNER = "sunday_dinner";

    private static final String DAY_MONDAY = "monday";
    private static final String DAY_TUESDAY = "tuesday";
    private static final String DAY_WEDNESDAY = "wednesday";
    private static final String DAY_THURSDAY = "thursday";
    private static final String DAY_FRIDAY = "friday";
    private static final String DAY_SATURDAY = "saturday";
    private static final String DAY_SUNDAY = "sunday";

    public enum Day {
        MONDAY(0,DAY_MONDAY),
        TUESDAY(1,DAY_TUESDAY),
        WEDNESDAY(2, DAY_WEDNESDAY),
        THURSDAY(3, DAY_THURSDAY),
        FRIDAY(4, DAY_FRIDAY),
        SATURDAY(5, DAY_SATURDAY),
        SUNDAY(6,DAY_SUNDAY);

        private String str;
        private int num;

        Day() {

        }

        Day(int num,String str) {
            this.str = new String(str);
            this.num = num;

        }

        public String getString() {
            return new String(this.str);
        }

        public int getNumber() {
            return this.num;
        }

        public static Day getDayFromString(String str) {
            Day rVal = null;
            if (str != null) {
                for (Day ie : Day.values()) {
                    if (ie.getString().compareToIgnoreCase(str) == 0) {
                        rVal = ie;
                        break;
                    }
                }
            }
            return rVal;
        }

        public static Day getDayFromInt(int n) {
            Day rVal = null;
            if (n >= 0) {
                for (Day ie : Day.values()) {
                    if (ie.getNumber() == n) {
                        rVal = ie;
                        break;
                    }
                }
            }
            return rVal;
        }


    }

    public G3OpeningHoursObj(){

    }

    public G3OpeningHoursObj(Day day,String key, Integer from, Integer to) {
        this.day = day;
        this.mKey = key;
        this.from = from; // format time using 800 for 8:00am or 2300 for 23:00
        this.to = to;
    }

    @Exclude
    public Day getDayAsEnum() {
        return day;
    }

    public String getDay(){
        if (day == null){
            return null;
        } else {
            return day.name();
        }
    }

    public void setDay(String daystring){
        if (daystring == null){
            day = null;
        } else {
            this.day = Day.valueOf(daystring);
        }
    }

    public void setDayAsEnum(Day day) {
        this.day = day;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
    }

    @Override
    public String toString() {
        return "OpeningHours [day=" + day + "key=" + mKey +", from=" + from + ", to=" + to + ", isAllDay=" + isAllDay + "]";
    }

    public boolean isOpen(DateTime start) {

        if (day.ordinal() != start.getDayOfWeek() - 1) {
            return false;
        }

        if (isAllDay)
            return true;

        String f = String.format("%04d", from);
        String t = String.format("%04d", to);

        Integer fh = Integer.valueOf(f.substring(0, 2));
        Integer fm = Integer.valueOf(f.substring(2));

        Integer th = Integer.valueOf(t.substring(0, 2));
        Integer tm = Integer.valueOf(t.substring(2));

        DateTime intStart = start.withHourOfDay(fh).withMinuteOfHour(fm);
        DateTime intEnd = start.withHourOfDay(th).withMinuteOfHour(tm);

        if (intStart.equals(start) || intEnd.equals(start)) {
            return true;
        }
        if (intStart.isBefore(start) && intEnd.isAfter(start)) {
            return true;
        }

        return false;
    }

    public ArrayList<String> getOrderingTimeSlots(int serviceTime) {

        ArrayList<String> timeSlots = new ArrayList<>();

        DateTime currentTime = new DateTime();

        int currentTimeHrs = currentTime.getHourOfDay();
        int currentTimeMin = currentTime.getMinuteOfHour();

        currentTimeHrs += serviceTime / 60;
        currentTimeMin += serviceTime % 60;
        if (currentTimeMin >= 60) {
            currentTimeMin = 0;
            currentTimeHrs += 1;
        }

        if (currentTimeMin > 0 && currentTimeMin < 15) {
            currentTimeMin = 15;
        } else if (currentTimeMin > 15 && currentTimeMin < 30) {
            currentTimeMin = 30;
        } else if (currentTimeMin > 30 && currentTimeMin < 45) {
            currentTimeMin = 45;
        } else if (currentTimeMin > 45 && currentTimeMin < 60) {
            currentTimeMin = 0;
            currentTimeHrs += 1;
        }

        int orderingTime = currentTimeHrs * 100 + currentTimeMin;

        if (orderingTime < from)
            orderingTime = from;

        while (orderingTime <= to) {

            timeSlots.add(getFormattedTime(orderingTime));
            orderingTime += 15;
            if (orderingTime % 100 == 60)
                orderingTime += 40;

        }

        return timeSlots;

    }

    private String getFormattedTime(int time) {

        int timeHrs = time / 100;
        int timeMin = time % 100;

        DecimalFormat df = new DecimalFormat("00");

        return df.format(timeHrs) + ":" + df.format(timeMin);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.day == null ? -1 : this.day.ordinal());
        dest.writeString(this.mKey);
        dest.writeInt(this.from);
        dest.writeInt(this.to);
        dest.writeByte(this.isAllDay ? (byte) 1 : (byte) 0);
    }

    protected G3OpeningHoursObj(Parcel in) {
        int tmpDay = in.readInt();
        this.day = tmpDay == -1 ? null : Day.values()[tmpDay];
        this.mKey = in.readString();
        this.from = in.readInt();
        this.to = in.readInt();
        this.isAllDay = in.readByte() != 0;
    }

    public static final Parcelable.Creator<G3OpeningHoursObj> CREATOR = new Parcelable.Creator<G3OpeningHoursObj>() {
        @Override
        public G3OpeningHoursObj createFromParcel(Parcel source) {
            return new G3OpeningHoursObj(source);
        }

        @Override
        public G3OpeningHoursObj[] newArray(int size) {
            return new G3OpeningHoursObj[size];
        }
    };

}
