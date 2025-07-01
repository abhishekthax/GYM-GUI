
/**
 * Project: VR Fitness Studio - Gym Management System
 * Author: Abhishek Kumar Thakur
 * Institution: Islington College
 *
 * Description:
 * The RegularMember class represents members who have opted for regular-tier plans
 * at VR Fitness Studio. It extends the base GymMember class and introduces features
 * specific to standard members.
 *
 * Key Features:
 * - Three plan tiers: Basic, Standard, Deluxe
 * - Attendance-based eligibility for plan upgrades
 * - Loyalty point tracking (5 points per attendance)
 * - Upgrade and revert functionality with price adjustment
 * - Referral source tracking and removal reason documentation
 *
 * Implementation Details:
 * - Inherits member identity and tracking from GymMember
 * - Uses loyaltyPoints to determine upgrade eligibility
 * - Contains static utility for plan pricing based on plan type
 * - Offers full member reset via revertRegularMember method
 *
 * Usage:
 * This class should be instantiated for members selecting regular plans.
 * It is fully integrated with GymMemberGUI for interaction through the GUI system.
 *
 * Educational Value:
 * - Demonstrates inheritance and method overriding in OOP
 * - Implements conditional logic for eligibility and plan transitions
 * - Provides real-world membership simulation logic
 *
 * Note:
 * Part of a coursework assignment to simulate gym membership handling with Java.
 */


public class RegularMember extends GymMember {
    private final int attendanceLimit = 30;
    private boolean isEligibleForUpgrade;
    private String removalReason;
    private String referralSource;
    private String plan;
    private double price;

    public RegularMember(int id, String name, String location, String phone, String email, 
                        String gender, String DOB, String membershipStartDate, String referralSource) {
        super(id, name, location, phone, email, gender, DOB, membershipStartDate);
        this.referralSource = referralSource;
        this.plan = "Basic";
        this.price = 6500;
        this.removalReason = "";
    }

    // Getters
    public int getAttendanceLimit() { return attendanceLimit; }
    public boolean isEligibleForUpgrade() { return isEligibleForUpgrade; }
    public String getRemovalReason() { return removalReason; }
    public String getReferralSource() { return referralSource; }
    public String getPlan() { return plan; }
    public double getPrice() { return price; }

    @Override
    public void markAttendance() {
        if (activeStatus) {
            attendance++;
            loyaltyPoints += 5;
            if (loyaltyPoints >= attendanceLimit) {
                isEligibleForUpgrade = true;
            }
        }
    }

    public static double getPlanPrice(String plan) {
        switch(plan.toLowerCase()) {
            case "basic": return 6500;
            case "standard": return 12500;
            case "deluxe": return 18500;
            default: return -1;
        }
    }

    public String upgradePlan(String newPlan) {
        if (!isEligibleForUpgrade) {
            return "Member is NOT eligible for upgrade";
        }
        if (newPlan.equalsIgnoreCase(plan)) {
            return "Member is already in the " + plan + " plan.";
        }
        double newPrice = getPlanPrice(newPlan);
        if (newPrice == -1) {
            return "Invalid plan";
        }
        plan = newPlan;
        price = newPrice;
        return "Plan upgraded to " + newPlan + " successfully";
    }

    public void revertRegularMember(String removalReason) {
        super.resetMember();
        isEligibleForUpgrade = false;
        plan = "Basic";
        price = 6500;
        this.removalReason = removalReason;
    }

    public void display() {
        super.display();
        System.out.println("Plan: " + plan);
        System.out.println("Price: " + price);
        if (!removalReason.isEmpty()) {
            System.out.println("Removal Reason: " + removalReason);
        }
    }
}