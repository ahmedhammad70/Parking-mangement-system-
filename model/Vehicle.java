package com.parking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class Vehicle {
    private int id;
    private String code;
    private String plate;
    private String type; 
    private String ownerName;
    private String ownerId;
    private LocalDateTime entryTime;
    private String attendant;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Vehicle() {
        this.entryTime = LocalDateTime.now();
    }

    public Vehicle(String plate, String type, String ownerName, String ownerId, String attendant) {
        this.id = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        this.code = generateCode();
        this.plate = plate.toUpperCase();
        this.type = type;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        this.entryTime = LocalDateTime.now();
        this.attendant = attendant;
    }
    /**
     * 
     * @return code for the vehicle
     */

    private String generateCode() {
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int random = (int) (Math.random() * 1000);
        return dateStr + "-" + String.format("%03d", random);
    }

    


    /**
     * 
     * @param hourlyRate for car 
     * @return total  fee of parking 
     */
    public double calculateFee(double hourlyRate) {
        long hours = Math.max(1, ChronoUnit.HOURS.between(entryTime, LocalDateTime.now()) + 1);
        return hours * hourlyRate;
    }
    /**
     * 
     * @return hours parked in the parking 
     */

    public long getHoursParked() {
        return Math.max(1, ChronoUnit.HOURS.between(entryTime, LocalDateTime.now()) + 1);
    }
    /**
     * 
     * @return vehicle id
     */

    
    public int getId() {
        return id;
    }
    /**
     * 
     * @param id of the vehicle 
     */

    public void setId(int id) {
        this.id = id;
    }
    /**
     * 
     * @return code of the vehicle 
     */

    public String getCode() {
        return code;
    }
    /**
     * 
     * @param code of the vehicle 
     */

    public void setCode(String code) {
        this.code = code;
    }
    /**
     * 
     * @return plate of the vehicle 
     */

    public String getPlate() {
        return plate;
    }
    /**
     * 
     * @param plate of the vehicle  
     */

    public void setPlate(String plate) {
        this.plate = plate.toUpperCase();
    }
    /**
     * 
     * @return vehicle type 
     */

    public String getType() {
        return type;
    }
    /**
     * 
     * @param type of vehicle 
     */

    public void setType(String type) {
        this.type = type;
    }
    /**
     * 
     * @return vehicle owner name 
     */

    public String getOwnerName() {
        return ownerName;
    }
    /**
     * 
     * @param  vehicle ownerName
     */

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    /**
     * 
     * @return vehicle owner id 
     */

    public String getOwnerId() {
        return ownerId;
    }
    /**
     * 
     * @param vehicle  ownerId
     */

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    /**
     * 
     * @return entry time of vehicle 
     */

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    /**
     * 
     * @param entryTime of vehicle 
     */

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
    

    public String getAttendant() {
        return attendant;
    }

    public void setAttendant(String attendant) {
        this.attendant = attendant;
    }

    public String getFormattedEntryTime() {
        return entryTime.format(formatter);
    }

    @Override
    public String toString() {
        return plate + " (" + type + ") - " + getFormattedEntryTime();
    }
}
