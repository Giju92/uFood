package com.example.ufoodlibrary.objects;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Mattia on 20/04/2016.
 */
public class G3PaymentMethods implements Parcelable {

    private boolean creditCard;
    private boolean money;
    private boolean bancomat;
    private boolean ticket;
    private String ticketsSupported;

    public G3PaymentMethods(){

    }

    public G3PaymentMethods(boolean creditCard, boolean money, boolean bancomat, boolean ticket, String ticketsSupported) {
        this.creditCard = creditCard;
        this.money = money;
        this.bancomat = bancomat;
        this.ticket = ticket;
        this.ticketsSupported = ticketsSupported;
    }

    public boolean isCreditCard() {
        return creditCard;
    }

    public void setCreditCard(boolean creditCard) {
        this.creditCard = creditCard;
    }

    public boolean isMoney() {
        return money;
    }

    public void setMoney(boolean money) {
        this.money = money;
    }

    public boolean isBancomat() {
        return bancomat;
    }

    public void setBancomat(boolean bancomat) {
        this.bancomat = bancomat;
    }

    public boolean isTicket() {
        return ticket;
    }

    public void setTicket(boolean ticket) {
        this.ticket = ticket;
    }

    public String getTicketsSupported() {
        return ticketsSupported;
    }

    public void setTicketsSupported(String ticketsSupported) {
        this.ticketsSupported = ticketsSupported;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.creditCard ? (byte) 1 : (byte) 0);
        dest.writeByte(this.money ? (byte) 1 : (byte) 0);
        dest.writeByte(this.bancomat ? (byte) 1 : (byte) 0);
        dest.writeByte(this.ticket ? (byte) 1 : (byte) 0);
        dest.writeString(this.ticketsSupported);
    }

    protected G3PaymentMethods(Parcel in) {
        this.creditCard = in.readByte() != 0;
        this.money = in.readByte() != 0;
        this.bancomat = in.readByte() != 0;
        this.ticket = in.readByte() != 0;
        this.ticketsSupported = in.readString();
    }

    public static final Parcelable.Creator<G3PaymentMethods> CREATOR = new Parcelable.Creator<G3PaymentMethods>() {
        @Override
        public G3PaymentMethods createFromParcel(Parcel source) {
            return new G3PaymentMethods(source);
        }

        @Override
        public G3PaymentMethods[] newArray(int size) {
            return new G3PaymentMethods[size];
        }
    };

}

