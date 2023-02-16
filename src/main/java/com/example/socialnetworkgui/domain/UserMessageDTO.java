package com.example.socialnetworkgui.domain;

public class UserMessageDTO {
    private String firstName;
    private String lastName;
    private String message;
    private String  date;

    private String status;

    public UserMessageDTO(String firstName, String lastName, String message, String date, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.message = message;
        this.date = date;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

