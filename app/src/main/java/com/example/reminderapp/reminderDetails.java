
// This is the class for storing details about a reminder
package com.example.reminderapp;

public class reminderDetails {

    // These are the private fields that store the reminder's information
    private String reminderName; // Name of the reminder
    private String reminderLocation; // Location of the reminder
    private String latitude; // Latitude of the reminder's location
    private String longitude; // Longitude of the reminder's location
    private String description; // Description of the reminder

    // Getter method for the reminder name
    public String getReminderName() {
        return reminderName;
    }

    // Setter method for the reminder name
    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    // Getter method for the reminder description
    public String getReminderDescription() {
        return description;
    }

    // Setter method for the reminder description
    public void setReminderDescription(String reminderDescription) {
        this.description =reminderDescription;
    }

    // Getter method for the reminder location
    public String getReminderLocation() {
        return reminderLocation;
    }

    // Setter method for the reminder location
    public void setReminderLocation(String reminderLocation) {
        this.reminderLocation = reminderLocation;
    }

    // Getter method for the reminder latitude
    public String getLatitude() {
        return latitude;
    }

    // Setter method for the reminder latitude
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    // Getter method for the reminder longitude
    public String getLongitude() {
        return longitude;
    }

    // Setter method for the reminder longitude
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    // Constructor for creating a new reminder with the given details
    public reminderDetails(String description,String reminderName, String reminderLocation, String latitude, String longitude)
    {
        // Assigning values to the private fields using the constructor's parameters
        this.reminderLocation=reminderLocation;
        this.reminderName=reminderName;
        this.latitude=latitude;
        this.longitude=longitude;
        this.description=description;
    }

}
