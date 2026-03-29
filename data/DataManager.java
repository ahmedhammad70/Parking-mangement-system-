package com.parking.data;

import com.parking.model.*;
import java.time.LocalDate;
import java.util.*;


public class DataManager {
    private static DataManager instance;

    
    private List<User> users;
    private List<Vehicle> vehicles;
    private List<HistoryRecord> history;
    private Settings settings;
    private User currentUser;
    private boolean gateOpen;

    
    private static final Map<String, List<String>> PERMISSIONS = new HashMap<>();
    static {
        PERMISSIONS.put("Admin", Arrays.asList("Dashboard", "Users", "Reports", "Settings", "Backups"));
        PERMISSIONS.put("Manager", Arrays.asList("Dashboard", "Reports", "Settings"));
        PERMISSIONS.put("Cashier", Arrays.asList("Dashboard"));
        PERMISSIONS.put("Security", Arrays.asList("Dashboard"));
    }

    private DataManager() {
        users = new ArrayList<>();
        vehicles = new ArrayList<>();
        history = new ArrayList<>();
        settings = new Settings();
        gateOpen = false;

    
        users.add(new User(1, "admin", "admin", "Admin"));
    }
    /**
     * 
     * @return instance of datamanager 
     */
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    /**
     * 
     * @param user username
     * @param user password
     * @return the user 
     */
    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) &&
                    user.getPassword().equals(password)) {
                currentUser = user;
                return user;
            }
        }
        return null;
    }

    public void logout() {
        currentUser = null;
    }
    /**
     * 
     * @return the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    /**
     * 
     * @param the current tab
     * @return true if allowed is not null and it contains tab; otherwise return false.
     */
    public boolean hasPermission(String tab) {
        if (currentUser == null)
            return false;
        List<String> allowed = PERMISSIONS.get(currentUser.getRole());
        return allowed != null && allowed.contains(tab);
    }
    /**
     * 
     * @return the allowed tabs for the currnt user
     */
    public List<String> getAllowedTabs() {
        if (currentUser == null)
            return new ArrayList<>();
        return PERMISSIONS.getOrDefault(currentUser.getRole(), new ArrayList<>());
    }

    /**
     * 
     * @return return all users of the system in a list
     */
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }
    /**
     * 
     * @param user that is going to be added
     */
    public void addUser(User user) {
        user.setId((int) (System.currentTimeMillis() % Integer.MAX_VALUE));
        users.add(user);
    }
    /**
     * 
     * @param userId that is going to be removed
     * @return remove the user that got the "user id"
     */
    public boolean deleteUser(int userId) {
        return users.removeIf(u -> u.getId() == userId);
    }
    /**
     * 
     * @param username of the user that is being searched for
     * @return the user with "username"
     */
    public User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 
     * @return all vehicles in the system
     */
    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }
    /**
     * 
     * @param vehicle thats being added
     * @return false if no capacity or vehicle already in system,true if it can be added
     */
    public boolean addVehicle(Vehicle vehicle) {
        
        if (findVehicle(vehicle.getPlate()) != null) {
            return false;
        }
        
        if (vehicles.size() >= settings.getCapacity()) {
            return false;
        }
        vehicles.add(vehicle);
        return true;
    }
    /**
     * 
     * @param plate of the vehicle getting removed
     * @return removed vehicle
     */
    public Vehicle removeVehicle(String plate) {
        Iterator<Vehicle> it = vehicles.iterator();
        while (it.hasNext()) {
            Vehicle v = it.next();
            if (v.getPlate().equalsIgnoreCase(plate)) {
                it.remove();
                return v;
            }
        }
        return null;
    }
    /**
     * 
     * @param plate of the vehicle the user is searching for
     * @return the car being searched for
     */
    public Vehicle findVehicle(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getPlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }

    /**
     * 
     * @return all history of the system
     */
    public List<HistoryRecord> getHistory() {
        return new ArrayList<>(history);
    }
    /**
     * 
     * @param record that is being added to the history record 
     */
    public void addHistoryRecord(HistoryRecord record) {
        history.add(0, record); 
    }
    /**
     * 
     * @return  all the history of the today in a list
     */
    public List<HistoryRecord> getTodayHistory() {
        List<HistoryRecord> today = new ArrayList<>();
        LocalDate todayDate = LocalDate.now();
        for (HistoryRecord r : history) {
            if (r.getExitTime() != null &&
                    r.getExitTime().toLocalDate().equals(todayDate)) {
                today.add(r);
            }
        }
        return today;
    }
    /**
     * 
     * @return the total revenue made form fees paid by users
     */
    public double getTodayRevenue() {
        double total = 0;
        for (HistoryRecord r : getTodayHistory()) {
            total += r.getFee();
        }
        return total;
    }

    /**
     * 
     * @return object of settings
     */
    public Settings getSettings() {
        return settings;
    }
    /**
     * 
     * @param settings to set settings 
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * 
     * @return boolean of gateopen attribute
     */
    public boolean isGateOpen() {
        return gateOpen;
    }
    /**
     * 
     * @param open to change gateopen attribute 
     */
    public void setGateOpen(boolean open) {
        this.gateOpen = open;
    }

    /**
     * 
     * @return the  number of working staff in the system
     */
    public int getStaffCount() {
        return (int) vehicles.stream().filter(v -> "Staff".equals(v.getType())).count();
    }
    /**
     * 
     * @return the number of students in the system
     */
    public int getStudentCount() {
        return (int) vehicles.stream().filter(v -> "Student".equals(v.getType())).count();
    }
    /**
     * 
     * @return number of vehicles in the system
     */
    public int getTotalParked() {
        return vehicles.size();
    }
    /**
     * 
     * @return number of remaining spots people can park in 
     */
    public int getRemainingSpots() {
        return Math.max(0, settings.getCapacity() - vehicles.size());
    }
}
