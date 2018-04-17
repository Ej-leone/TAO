package com.githinji.ej.ezzysacco11.Database;

/**
 * Created by moses on 7/1/2016.
 */
public class userStatus {
    public String ustatas;
    public String usercode;
    public String pword;


    public  userStatus()
    {

    }

    public userStatus(String ustatas, String usercode,String pword)
    {
        this.ustatas =ustatas;
        this.usercode =usercode;
        this.pword = pword;


    }


    /*Set functions*/
    public void setustatas(String ustatas){this.ustatas =ustatas;}

    public void setusercode(String usercode) {
        usercode = usercode;
    }

    public void setpword(String pword) {
        this.pword = pword;
    }




    /*get Functions */
    public String getustatas(){ return ustatas;}

    public String getusercode() {
        return usercode;
    }

    public String getpword() {
        return pword;
    }



}
