package com.example.utu.ututrail;

/**
 * class for train
 *
 */
public class TrainType {

    private String type;
    private String name;
    private String service;
    private String price;
    private String duration;

    public String getName() {
        return name;
    }
    public void setName(String mName) {
        this.name = mName;
    }

    public String getType() {
        return type;
    }
    public void setType(String mType) {
        this.type = mType;
    }

    public void setService(String mService) {
        this.service = mService;
    }
    public String getService() { return service; }


    public String getPrice() { return price; }
    public void setPrice(String mPrice) {
        this.price = mPrice;
    }

    public String getDuration() { return duration; }
    public void setDuration(String mDuration) {
        this.duration = mDuration;
    }



}
