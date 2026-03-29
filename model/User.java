package com.parking.model;


public class User {
    private int id;
    private String username;
    private String password;
    private String role; 

    public User() {}

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * 
     * @return id of user
     */
    public int getId() { return id; }
    /**
     * 
     * @param id of user
     */
    public void setId(int id) { this.id = id; }
    /**
     * 
     * @return user username 
     */

    public String getUsername() { return username; }
    /**
     * 
     * @param username of the user
     */
    public void setUsername(String username) { this.username = username; }
    /**
     * 
     * @return password of user
     */

    public String getPassword() { return password; }
    /**
     * 
     * @param password of the user
     */
    public void setPassword(String password) { this.password = password; }
    /**
     * 
     * @return the user role 
     */

    public String getRole() { return role; }
    /**
     * 
     * @param role of the user
     */
    public void setRole(String role) { this.role = role; }

    @Override
    /**
     * 
     */
    public String toString() {
        return username + " - " + role;
    }
}
