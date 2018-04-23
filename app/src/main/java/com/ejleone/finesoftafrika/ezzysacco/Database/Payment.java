package com.ejleone.finesoftafrika.ezzysacco.Database;

/**
 * Created by EJ on 5/10/2016.
 */
public class Payment
{
    public String Amount;
    public String uuname;
    public String type;
    public  String customerno;
    public String receiptno;
    public String tupe;

    //Constructors

    public  Payment()
    {

    }

    public Payment(String Amount, String uuname,String type,String customerno,String receiptno, String tupe)
   {
        this.Amount =Amount;
        this.uuname =uuname;
        this.customerno = customerno;
        this.type =type;
        this.receiptno =receiptno;
        this.tupe = tupe;

    }


    /*Set functions*/
    public void setTupe(String tupe){this.tupe =tupe;}

    public void setAmount(String amount) {
        Amount = amount;
    }

    public void setUuname(String uuname) {
        this.uuname = uuname;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReceiptno(String receiptno) {
        this.receiptno = receiptno;
    }

    public void setCustomerno(String customerno) {
        this.customerno = customerno;
    }



    /*get Functions */
    public String getTupe(){ return tupe;}

    public String getAmount() {
        return Amount;
    }

    public String getUuname() {
        return uuname;
    }

    public String getType() {
        return type;
    }

    public String getReceiptno() {
        return receiptno;
    }

    public String getCustomerno() {
        return customerno;
    }
}

