package com.martynhaigh.sms.lib;

import android.os.Parcel;
import android.os.Parcelable;

public class SmsData implements Parcelable {

    private String body;
    private String number;


    public SmsData() {
    }

    private SmsData(Parcel in) {
        readFromParcel(in);
    }


    public void readFromParcel(Parcel in) {
        body = in.readString();
        number = in.readString();
    }

    public static final Parcelable.Creator<SmsData> CREATOR = new
            Parcelable.Creator<SmsData>() {
                public SmsData createFromParcel(Parcel in) {
                    return new SmsData(in);
                }

                public SmsData[] newArray(int size) {
                    return new SmsData[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(body);
        parcel.writeString(number);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}