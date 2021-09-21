package com.example.g_summerassignmnet.Testbooth_location;

public class LocationModel {



   private int image;
   private double lat;
   private double log;


    public LocationModel(int image, double lat, double log)
    {
        this.image = image;
        this.lat = lat;
        this.log = log;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "image=" + image +
                ", lat=" + lat +
                ", log=" + log +
                '}';
    }
}
