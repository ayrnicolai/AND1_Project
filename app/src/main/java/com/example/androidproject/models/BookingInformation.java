package com.example.androidproject.models;

public class BookingInformation {
    private String customerName, customerPhone, time, consultantId, consultantName, roomId, roomName, roomAddress;
    private Long slot;

    public BookingInformation() {
    }

    public BookingInformation(String customerName, String customerPhone, String time, String consultantId, String consultantName, String roomId, String roomName, String roomAddress, Long slot) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.time = time;
        this.consultantId = consultantId;
        this.consultantName = consultantName;
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomAddress = roomAddress;
        this.slot = slot;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(String consultantId) {
        this.consultantId = consultantId;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomAddress() {
        return roomAddress;
    }

    public void setRoomAddress(String roomAddress) {
        this.roomAddress = roomAddress;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
