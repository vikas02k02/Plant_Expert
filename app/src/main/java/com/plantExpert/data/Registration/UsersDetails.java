package com.plantExpert.data.Registration;

public class UsersDetails {
    private String mobileNumber , CityName ,FullName;

    public UsersDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(UsersDetails.class)
    }


    public UsersDetails(String mobileNumber, String cityName, String fullName) {
        this.mobileNumber = mobileNumber;
        CityName = cityName;
        FullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}
