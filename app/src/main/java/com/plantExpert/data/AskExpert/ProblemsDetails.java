package com.plantExpert.data.AskExpert;

import android.net.Uri;

public class ProblemsDetails {
    private String PlantName ;
    private String CityName;
    private String imageUri;
    ProblemsDetails(){};

    public ProblemsDetails(String plantName, String cityName, String imageUri) {

        PlantName = plantName;
        CityName = cityName;
        this.imageUri = imageUri;
    };

    public String getPlantName() {
        return PlantName;
    }

    public void setPlantName(String plantName) {
        PlantName = plantName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
