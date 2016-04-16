package com.town.small.brewtopia.DataClass;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.town.small.brewtopia.R;

/**
 * Created by Andrew on 3/7/2015.
 */
public class BoilAdditionsSchema {

    private long additionId=-1;
    private String brewName;
    private long userId=-1;
    private String additionName = "";
    private int additionTime = 0;
    private double additionQty = 0.0;
    private String uOfM;

    public BoilAdditionsSchema() {
    }

    //getters

    public String getAdditionName() {
        return additionName;
    }
    public int getAdditionTime() {
        return additionTime;
    }
    public String getBrewName() {
        return brewName;
    }
    public long getUserId() {
        return userId;
    }
    public double getAdditionQty() {
        return additionQty;
    }
    public String getUOfM() {
        return uOfM;
    }
    public long getAdditionId() {
        return additionId;
    }


    //setters
    public void setAdditionName(String additionName) {
        this.additionName = additionName;
    }
    public void setAdditionTime(int additionTime) {
        this.additionTime = additionTime;
    }
    public void setBrewName(String brewName) {
        this.brewName = brewName;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public void setAdditionQty(double additionQty) {
        this.additionQty = additionQty;
    }
    public void setUOfM(String uOfM) {
        this.uOfM = uOfM;
    }
    public void setAdditionId(long additionId) {
        this.additionId = additionId;
    }

}
