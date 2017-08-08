package com.coding4fun.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by coding4fun on 22-Oct-16.
 */

public class Resturant implements Parcelable {

    private String name,password,ownerName,phoneNumber,email,address;
    Map<String,Boolean> services = new HashMap<>();
    Map<String,Boolean> paymentMethods = new HashMap<>();

    public static final String SERVICE_DELIVERY = "delivery";
    public static final String SERVICE_TAKEAWAY = "takeAway";
    public static final String SERVICE_BOOKTABLE = "bookTable";
    public static final String PAYMENT_CASH = "cash";
    public static final String PAYMENT_VISA = "visa";

    public Resturant(){}

    public Resturant(String name, String email, String password, String ownerName, String phoneNumber, String address, Map<String, Boolean> services, Map<String, Boolean> paymentMethods) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.ownerName = ownerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.services = services;
        this.paymentMethods = paymentMethods;
    }

    public Resturant(String name, String email, String password, String ownerName, String phoneNumber, String address, boolean delivery, boolean takeAway, boolean bookTable, boolean cash, boolean visa) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.ownerName = ownerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        /*if(delivery)*/ services.put(SERVICE_DELIVERY,delivery);
        /*if(takeAway)*/ services.put(SERVICE_TAKEAWAY,takeAway);
        /*if(bookTable)*/ services.put(SERVICE_BOOKTABLE,bookTable);
        paymentMethods.put(PAYMENT_CASH,cash);
        paymentMethods.put(PAYMENT_VISA,visa);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public Map<String, Boolean> getServices() {
        return services;
    }

    public void setServices(Map<String, Boolean> services) {
        this.services = services;
    }

    public Map<String, Boolean> getPaymentMethods() {return paymentMethods;}

    public void setPaymentMethods(Map<String, Boolean> paymentMethods) {this.paymentMethods = paymentMethods;}

    /*************** PARCELABLE STUFF ***************/

    @Override
    public int describeContents() {
        return 0;
    }

    //Constructor to use when re-constructing object from a parcel
    public Resturant(Parcel in){
        readFromParcel(in);
    }

    //Called from the constructor to create this object from a parcel.
    private void readFromParcel(Parcel in) {
        // We just need to read back each field in the order that it was written to the parcel
        name = in.readString();
        email = in.readString();
        password = in.readString();
        ownerName = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        int size;
        //read services
        size = in.readInt();
        for(int i=0;i<size;i++){
            String key = in.readString();
            boolean value = (in.readInt() == 1) ? true : false;
            services.put(key,value);
        }
        //read paymentMethods
        size = in.readInt();
        for(int i=0;i<size;i++){
            String key = in.readString();
            boolean value = (in.readInt() == 1) ? true : false;
            paymentMethods.put(key,value);
        }
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // We just need to write each field into the parcel. When we read from parcel, they will come back in the same order
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(ownerName);
        parcel.writeString(phoneNumber);
        parcel.writeString(address);
        //write services
        int servicesSize = services.size();
        parcel.writeInt(servicesSize);
        for(String key : services.keySet()){
            parcel.writeString(key);
            //write 1 if true, 0 otherwise. cz no writeBoolean
            parcel.writeInt((services.get(key)) ? 1 : 0);
        }
        //write paymentMethods
        int paymentMethodsSize = paymentMethods.size();
        parcel.writeInt(paymentMethodsSize);
        for(String key : paymentMethods.keySet()){
            parcel.writeString(key);
            //write 1 if true, 0 otherwise. cz no writeBoolean
            parcel.writeInt((paymentMethods.get(key)) ? 1 : 0);
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Resturant createFromParcel(Parcel in){
            return new Resturant(in);
        }
        public Resturant[] newArray(int size){
            return new Resturant[size];
        }
    };
}