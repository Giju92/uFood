package com.example.ufoodlibrary.objects;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alfonso on 01-Apr-16.
 */
public class G3UserObj implements Parcelable {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private String mobileNumber;
    private String password;
    private String photoPath;

    public G3UserObj(){

    }

    public G3UserObj(String firstName, String lastName, String username, String email, String phoneNumber, String mobileNumber, String password, String photoPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.photoPath = photoPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.mobileNumber);
        dest.writeString(this.password);
        dest.writeString(this.photoPath);
    }

    protected G3UserObj(Parcel in) {
        this.id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.username = in.readString();
        this.email = in.readString();
        this.phoneNumber = in.readString();
        this.mobileNumber = in.readString();
        this.password = in.readString();
        this.photoPath = in.readString();
    }

    public static final Parcelable.Creator<G3UserObj> CREATOR = new Parcelable.Creator<G3UserObj>() {
        @Override
        public G3UserObj createFromParcel(Parcel source) {
            return new G3UserObj(source);
        }

        @Override
        public G3UserObj[] newArray(int size) {
            return new G3UserObj[size];
        }
    };

}
