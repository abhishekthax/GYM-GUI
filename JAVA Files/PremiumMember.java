

/**
 * Project: VR Fitness Studio - Gym Management System
 * Author: Abhishek Kumar Thakur
 * Institution: Islington College

 * 
 * Description:
 * The PremiumMember class represents premium-tier members of VR Fitness Studio,
 * extending the base GymMember class with enhanced features and privileges.
 * 
 * Key Features:
 * - Premium membership with fixed charge of NPR 50,000
 * - Personal trainer assignment and management
 * - Full payment tracking with 10% discount eligibility
 * - Advanced attendance tracking with loyalty points (10 points per attendance)
 * - Payment processing with due amount calculation
 * - Comprehensive member status reporting
 * 
 * Implementation Details:
 * - Inherits all core member attributes from GymMember
 * - Implements payment tracking system with discount calculation
 * - Provides methods for membership reversion/reset
 * - Includes GUI-compatible methods for integration with management system
 * - Maintains strict encapsulation with proper access modifiers
 * 
 * Usage:
 * This class should be instantiated for members opting for premium services.
 * It integrates with the main GymMemberGUI system for complete management.
 * 
 * Educational Value:
 * - Demonstrates inheritance in OOP
 * - Shows proper encapsulation techniques
 * - Implements business logic for membership systems
 * - Provides practical example of class design
 * 
 * Note:
 * This implementation is part of a coursework project demonstrating
 * object-oriented programming principles in Java.
 */
public class PremiumMember extends GymMember {
    private final double premiumCharge = 50000;
    private String personalTrainer;
    private boolean isFullPayment;
    private double paidAmount;
    private double discountAmount;

    public PremiumMember(int id, String name, String location, String phone, String email, 
                        String gender, String DOB, String membershipStartDate, String personalTrainer) {
        super(id, name, location, phone, email, gender, DOB, membershipStartDate);
        this.personalTrainer = personalTrainer;
        this.isFullPayment = false;
        this.paidAmount = 0;
        this.discountAmount = 0;
    }

    // Getters
    public double getPremiumCharge() { return premiumCharge; }
    public String getPersonalTrainer() { return personalTrainer; }
    public boolean isFullPayment() { return isFullPayment; }
    public double getPaidAmount() { return paidAmount; }
    public double getDiscountAmount() { return discountAmount; }

    @Override
    public void markAttendance() {
        if (activeStatus) {
            attendance++;
            loyaltyPoints += 10;
        }
    }

    public String payDueAmount(double amount) {
        if (isFullPayment) {
            return "Payment is successful. No further payments required.";
        }
        if (amount <= 0) {
            return "Please enter a valid amount.";
        }
        if (paidAmount + amount > premiumCharge) {
            return "Payment exceeds the premium charge. Maximum allowed is " + (premiumCharge - paidAmount);
        }
        
        paidAmount += amount;
        isFullPayment = (paidAmount == premiumCharge);
        if (isFullPayment) {
            calculateDiscount();
        }
        double remaining = premiumCharge - paidAmount;
        return "Payment of " + amount + " received. Remaining amount: " + remaining;
    }

    public double getDueAmount() {
        return premiumCharge - paidAmount;
    }

    public void calculateDiscount() {
        if (isFullPayment) {
            discountAmount = premiumCharge * 0.10;
        } else {
            discountAmount = 0;
        }
    }

    public void revertPremiumMember() {
        super.resetMember();
        personalTrainer = "";
        isFullPayment = false;
        paidAmount = 0;
        discountAmount = 0;
    }
    

    @Override
    public void display() {
        super.display();
        System.out.println("Personal Trainer: " + personalTrainer);
        System.out.println("Paid Amount: " + paidAmount);
        System.out.println("Is Full Payment: " + isFullPayment);
        System.out.println("Remaining Amount: " + (premiumCharge - paidAmount));
        if (isFullPayment) {
            System.out.println("Discount Amount: " + discountAmount);
        }
    }
}
