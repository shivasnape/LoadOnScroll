package com.example.shiva.loadingdatasonscrolling.model;

/**
 * Created by root on 28/7/17.
 */

public class NotificationDataList {


    String sMessage, sCreatedBy, sNotificationName, sDate, sTime,sAging;


    public NotificationDataList() {

    }

    public NotificationDataList(String message, String createdBy, String notificationName, String date, String time, String aging) {

        this.sMessage = message;
        this.sCreatedBy = createdBy;
        this.sNotificationName = notificationName;
        this.sDate = date;
        this.sTime = time;
        this.sAging = aging;
    }

    public String getsMessage() {
        return sMessage;
    }

    public void setsMessage(String sMessage) {
        this.sMessage = sMessage;
    }

    public String getsCreatedBy() {
        return sCreatedBy;
    }

    public void setsCreatedBy(String sCreatedBy) {
        this.sCreatedBy = sCreatedBy;
    }

    public String getsNotificationName() {
        return sNotificationName;
    }

    public void setsNotificationName(String sNotificationName) {
        this.sNotificationName = sNotificationName;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String getsAging() {
        return sAging;
    }

    public void setsAging(String sAging) {
        this.sAging = sAging;
    }
}
