package com.example.g_summerassignmnet.Testbooth_location;

public class TestboothModel {

  String b_name;
  String b_address;
  String b_lat;
  String b_log;

    public TestboothModel(String b_name, String b_address, String b_lat, String b_log) {
        this.b_name = b_name;
        this.b_address = b_address;
        this.b_lat = b_lat;
        this.b_log = b_log;
    }

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getB_address() {
        return b_address;
    }

    public void setB_address(String b_address) {
        this.b_address = b_address;
    }

    public String getB_lat() {
        return b_lat;
    }

    public void setB_lat(String b_lat) {
        this.b_lat = b_lat;
    }

    public String getB_log() {
        return b_log;
    }

    public void setB_log(String b_log) {
        this.b_log = b_log;
    }

    @Override
    public String toString() {
        return "TestboothModel{" +
                "b_name='" + b_name + '\'' +
                ", b_address='" + b_address + '\'' +
                ", b_lat='" + b_lat + '\'' +
                ", b_log='" + b_log + '\'' +
                '}';
    }
}
