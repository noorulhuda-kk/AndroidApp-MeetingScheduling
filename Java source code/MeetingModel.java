package com.example.meetingsceduling;

public class MeetingModel {
    private String title;
    private String date;
    private String startTime;
    private String endTime;
    private String employee;
    private String location;
    private String description;
    private String status;

    public MeetingModel(String title, String date, String startTime, String endTime, String employee, String location, String description, String status) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employee = employee;
        this.location = location;
        this.description = description;
        this.status = status;
    }

    // Getters and setters for all fields
    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getEmployee() {
        return employee;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
