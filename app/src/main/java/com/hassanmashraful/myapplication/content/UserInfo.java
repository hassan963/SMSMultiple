package com.hassanmashraful.myapplication.content;

/**
 * Created by Hassan M.Ashraful on 2/1/2017.
 */

public class UserInfo {

    ///"contacts":[{"id":"1","number":"8801957111708","sms":"test sms 1 ","status":"1"},{"id":"2","number":"8801913185094","sms":"test sms 2 ","status":"1"}]}

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
