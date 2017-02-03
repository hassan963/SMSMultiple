package com.hassanmashraful.myapplication.content;

/**
 *
 * Hassan M Ashraful
 * Email: ashraful963@gmail.com
 * SMS GATEWAY
 */

public class UserInfo {



    private String number, sms, status, id;

    public UserInfo(String id, String number, String sms, String status){
        setId(id); setNumber(number); setSms(sms); setStatus(status);

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

    private void setNumber(String number) {
        this.number = number;
    }

    public String getSms() {
        return sms;
    }

    private void setSms(String sms) {
        this.sms = sms;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }
}
