package com.parking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class HistoryRecord {
    private int id;
    private String plate;
    private String ownerName;
    private String ownerId;
    private String type;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double fee;
    private String entryAttendant;
    private String exitAttendant;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HistoryRecord() {
    }

    public HistoryRecord(Vehicle vehicle, double fee, String exitAttendant) {
        this.id = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        this.plate = vehicle.getPlate();
        this.ownerName = vehicle.getOwnerName();
        this.ownerId = vehicle.getOwnerId();
        this.type = vehicle.getType();
        this.entryTime = vehicle.getEntryTime();
        this.exitTime = LocalDateTime.now();
        this.fee = fee;
        this.entryAttendant = vehicle.getAttendant();
        this.exitAttendant = exitAttendant;
    }

    /**
     * 
     * @return histort record id 
     */
    public int getId() {
        return id;
    }
    /**
     * 
     * @param id of the record
     */

    public void setId(int id) {
        this.id = id;
    }
/**
 * 
 * @return plate of the car in the record
 */
    public String getPlate() {
        return plate;
    }
/**
 * 
 * @param plate of the car in the record
 */
    public void setPlate(String plate) {
        this.plate = plate;
    }
    /**
     * 
     * @return owner of the car in the record
     */

    public String getOwnerName() {
        return ownerName;
    }
    /**
     * 
     * @param ownerName of the car in the record
     */

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    /**
     * 
     * @return the id of the car owner in the record
     */

    public String getOwnerId() {
        return ownerId;
    }
    /**
     * 
     * @param ownerId of the car owner in the car
     */

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    /**
     * 
     * @return type 
     */

    public String getType() {
        return type;
    }
    /**
     * 
     * @param  to set type
     */

    public void setType(String type) {
        this.type = type;
    }
    /**
     * 
     * @return the entry time of the car
     */

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    /**
     * 
     * @param entryTime of the car in the record to be set
     */

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
    /**
     * 
     * @return teh exit time of the car in ther record
     */

    public LocalDateTime getExitTime() {
        return exitTime;
    }
    /**
     * 
     * @param exitTime of the car in the record
     */

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
    /**
     * 
     * @return fee of parking
     */

    public double getFee() {
        return fee;
    }
    /**
     * 
     * @param fee of parking
     */

    public void setFee(double fee) {
        this.fee = fee;
    }
    /**
     * 

     */

    public String getEntryAttendant() {
        return entryAttendant;
    }

    public void setEntryAttendant(String entryAttendant) {
        this.entryAttendant = entryAttendant;
    }

    public String getExitAttendant() {
        return exitAttendant;
    }

    public void setExitAttendant(String exitAttendant) {
        this.exitAttendant = exitAttendant;
    }

    public String getFormattedEntryTime() {
        return entryTime != null ? entryTime.format(formatter) : "-";
    }

    public String getFormattedExitTime() {
        return exitTime != null ? exitTime.format(formatter) : "-";
    }

    // For CSV export
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s,%s,%.2f",
                plate, ownerName, ownerId, type, getFormattedEntryTime(), getFormattedExitTime(), fee);
    }
}
