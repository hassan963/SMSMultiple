package com.hassanmashraful.myapplication.content;

/**
 *
 * Hassan M Ashraful
 * Email: ashraful963@gmail.com
 * SMS GATEWAY
 */

public class UserInfo {



    private String number, sms, status, id;

    public UserInfo() {
    }

    public UserInfo(String id, String number, String sms, String status){
        setId(id); setNumber(number); setSms(sms); setStatus(status);

    }

    public UserInfo(String number, String sms){
        setSms(sms); setNumber(number);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }
}
