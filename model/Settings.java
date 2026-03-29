package com.parking.model;


public class Settings {
    private String companyName;
    private int capacity;
    private double staffRate;
    private double studentRate;
    private String currency;
    private String bannerPath;
    private String logoPath;

    public Settings() {
        
        this.companyName = "Parking System";
        this.capacity = 100;
        this.staffRate = 20.0;
        this.studentRate = 10.0;
        this.currency = "EGP";
        this.bannerPath = "";
        this.logoPath = "";
    }

    public Settings(String companyName, int capacity, double staffRate, double studentRate) {
        this.companyName = companyName;
        this.capacity = capacity;
        this.staffRate = staffRate;
        this.studentRate = studentRate;
        this.currency = "EGP";
        this.bannerPath = "";
        this.logoPath = "";
    }

 /**
  * 
  * @return the company name using the system
  */   
    public String getCompanyName() {
        return companyName;
    }
/**
 * 
 * @param companyName of the company usin the system
 */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    /**
     * 
     * @return the capacity of the parking area 
     */

    public int getCapacity() {
        return capacity;
    }
/**
 * 
 * @param capacity of the parking area
 */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    /**
     * 
     * @return staff rate for parking to calculate fee 
     */

    public double getStaffRate() {
        return staffRate;
    }
/**
 * 
 * @param staffRate to calculate fee 
 */
    public void setStaffRate(double staffRate) {
        this.staffRate = staffRate;
    }
    /**
     * 
     * @return rate for parking to calculate fee 
     */

    public double getStudentRate() {
        return studentRate;
    }
    /**
 * 
 * @param studentrate  to calculate fee 
 */

    public void setStudentRate(double studentRate) {
        this.studentRate = studentRate;
    }
    /**
     * 
     * @return currency used for rate calculation
     */

    public String getCurrency() {
        return currency;
    }
    /**
     * 
     * @param currency for rate calculation
     */

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    /**
     * 
     * @return banner path of organization using the system
     */

    public String getBannerPath() {
        return bannerPath;
    }
    /**
     * 
     * @param bannerPath of the organization using the system
     */

    public void setBannerPath(String bannerPath) {
        this.bannerPath = bannerPath;
    }
    /**
     * 
     * @return logo path of organization using the system
     */

    public String getLogoPath() {
        return logoPath;
    }
    /**
     * 
     * @param logoPath of organization using the system
     */

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
    /**
     * 
     * @param vehicleType the will get its rate called
     * @return staff rate if staff return student rate if student according to vehicle type
     */

    public double getRate(String vehicleType) {
        return "Staff".equalsIgnoreCase(vehicleType) ? staffRate : studentRate;
    }
}
