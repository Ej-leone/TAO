package com.ejleone.finesoftafrika.ezzysacco.Database;

/**
 * Created by EJ on 5/10/2016.
 * used for the atabse
 *
 */
public class User
{
    public  String uname;
    public String status;
    public  String uuname;
    public  String Pid;
    public  String Customerno;
    public String Pnum ;

    //constructors

    public  User()
    {
    }


    public User(String Pid,String Customerno,String Pnum)
    {
       // this.upnum =upnum;
        this.uuname =uuname;
        this.Pid =Pid;
        this.Customerno =Customerno;
        this.Pnum=Pnum;

    }

    // Setter function


    public void setStatus(String status) {
        this.status = status;
    }

    public void setUuname(String uuname) {
        this.uuname = uuname;
    }

    public void setCustomerno(String customerno) {
        Customerno = customerno;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

   /* public void setUpnum(int upnum) {
        this.upnum = upnum;
    }*/

    public void setPnum(String pnum) {
        Pnum = pnum;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }


    //getter functions


    public String getStatus() {
        return status;
    }

    public String getUuname() {
        return uuname;
    }

    public String getCustomerno() {
        return Customerno;
    }

    public String getPid() {
        return Pid;
    }

    public String getPnum() {
        return Pnum;
    }

   /* public int getUpnum() {
        return upnum;
    }*/

    public String getUname() {
        return uname;
    }
}

