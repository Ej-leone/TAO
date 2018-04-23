package com.ejleone.finesoftafrika.ezzysacco.Database;

/**
 * Created by EJ on 5/14/2016.
 */
public class Product
{
    public String prodname;
    public int procode;

    //constructor

    public  Product(){}

    //set function
    public void setProcode(int procode) {
        this.procode = procode;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }


    //get functions
    public int getProcode() {
        return procode;
    }

    public String getProdname() {
        return prodname;
    }
}
