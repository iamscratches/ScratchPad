package com.iamscratches.scratchpad;

public class AdapterItems {
    public int ID;
    public String date;
    public String item;
    public String quantity;
    public float amount;

    //for news details
    AdapterItems( int ID, String date, String item, String quantity, float amount)
    {
        this. ID=ID;
        this.date = date;
        this.item = item;
        this.quantity = quantity;
        this.amount = amount;
    }
}

