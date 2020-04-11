package com.polytech.copill;

public class Detail {

    private String pillName,pillHour;
    private int pillQuantity;

    public Detail(String parName, int parQuantity, String parHour){
        pillName=parName;
        pillQuantity=parQuantity;
        pillHour=parHour;
    }

    public void setName (String parName){
        pillName=parName;
    }

    public void setQuantity (int parQuantity){
        pillQuantity=parQuantity;
    }

    public void setHour (String parHour){
        pillHour=parHour;
    }

    public String getName (){
        return pillName;
    }

    public int getQuantity (){
        return pillQuantity;
    }

    public String getHour (){
        return pillHour;
    }
}
