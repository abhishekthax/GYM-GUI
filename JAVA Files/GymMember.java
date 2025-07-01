
/**
 * Project: VR Fitness Studio - Gym Management System
 * Author: Abhishek Kumar Thakur
 * Institution: Islington College
 *
 * Description:
 * The GymMember class is the abstract superclass that provides the foundational structure
 * for both RegularMember and PremiumMember types at VR Fitness Studio.
 *
 * Key Features:
 * - Common attributes such as ID, name, contact info, gender, and membership dates
 * - Attendance tracking and loyalty points system
 * - Active/inactive membership status handling
 * - Abstract method for marking attendance (implemented differently by subclasses)
 * - Member reset mechanism used during reversion
 * - Basic display method for command-line reporting
 *
 * Implementation Details:
 * - Serves as a base class with shared functionality and enforced method contracts
 * - Abstract `markAttendance()` must be defined by subclasses
 * - Additional methods are stubbed for GUI compatibility and overridden in subclasses
 *
 * Usage:
 * This class should be extended by any specific member types (e.g., RegularMember, PremiumMember)
 * to inherit and build upon the shared core logic for all members of the fitness studio.
 *
 * Educational Value:
 * - Demonstrates object-oriented design using abstraction and inheritance
 * - Promotes code reuse by encapsulating common logic
 * - Sets a contract for future extensions of the system
 *
 * Note:
 * This class is part of a coursework project designed to showcase principles of
 * inheritance, abstraction, and GUI interaction in Java-based applications.
 */


public abstract class GymMember {
    protected int id;
    protected String DOB;
    protected String name;
    protected String membershipStartDate;
    protected String location;
    protected int attendance;
    protected String phone;
    protected double loyaltyPoints;
    protected String email;
    protected boolean activeStatus;
    protected String gender;

    public GymMember(int id, String name, String location, String phone, String email, 
                    String gender, String DOB, String membershipStartDate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.DOB = DOB;
        this.membershipStartDate = membershipStartDate;
        this.attendance = 0;
        this.loyaltyPoints = 0;
        this.activeStatus = true;
    }

    // Getters
    public int getID() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getDOB() { return DOB; }
    public String getMembershipStartDate() { return membershipStartDate; }
    public int getAttendance() { return attendance; }
    public int getAttendanceCount() { return attendance; }
    public double getLoyaltyPoints() { return loyaltyPoints; }
    public boolean getActiveStatus() { return activeStatus; }

    public abstract void markAttendance();

    public void activeMembership() {
        activeStatus = true;
    }

    public void deactivateMembership() {
        activeStatus = false;
    }

    public void resetMember() {
        activeStatus = false;
        loyaltyPoints = 0;
        attendance = 0;
    }

    public void display() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("DOB: " + DOB);
        System.out.println("Location: " + location);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Gender: " + gender);
        System.out.println("Attendance: " + attendance);
        System.out.println("Membership Start Date: " + membershipStartDate);
        System.out.println("Loyalty Points: " + loyaltyPoints);
        System.out.println("Active Status: " + activeStatus);
    }
    
    // Added new methods for GUI compatibility
    public boolean getFullPayment() {
        return false; // Default implementation for base class
    } 
    
    public double getDiscountAmount() {
        return 0; // Default implementation for base class
    }
    
    public double getNetAmountPaid() {
        return 0; // Default implementation for base class
    }
}