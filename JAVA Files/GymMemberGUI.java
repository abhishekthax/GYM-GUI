

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
/**
 * Project: VR Fitness Studio - Gym Management System
 * Author: Abhishek Kumar Thakur
 * Institution: Islington College
 *
 * Description:
 * The GymMemberGUI class provides a graphical user interface for managing gym members 
 * in the VR Fitness Studio system. It supports registration, management, and tracking 
 * features for both Regular and Premium members.
 *
 * Key Features:
 * - GUI-based registration form for Regular and Premium members
 * - Attendance marking and loyalty point tracking
 * - Membership activation and deactivation
 * - Plan upgrades and premium reversion functionality
 * - Payment handling with discount calculation for Premium members
 * - Data persistence through file save and read functionality
 * - Distinct input sections based on member type with validation
 * 
 * Implementation Details:
 * - Built using Java Swing components (JFrame, JPanel, JButton, JComboBox, JTextField, etc.)
 * - Uses event-driven programming via ActionListeners
 * - Applies OOP concepts including encapsulation, inheritance, and polymorphism
 * - Stores member data using an ArrayList for runtime and writes to a text file for persistence
 * 
 * Usage:
 * This class should be run as the main GUI class to handle all front-end operations
 * related to gym membership management.
 * 
 * Educational Value:
 * - Demonstrates GUI application development using Java Swing
 * - Applies exception handling, validation, and data I/O operations
 * - Reinforces concepts of modular design and class integration
 * - Provides a complete example of combining backend logic with a user interface
 *
 * Note:
 * This class is developed as part of coursework for demonstrating object-oriented
 * programming, GUI design, and user interaction handling in Java.
 */


public class GymMemberGUI extends JFrame {
    private List<GymMember> members = new ArrayList<>();
    private JTextArea displayArea;
    private JTextField idField, nameField, locationField, phoneField, emailField;
    private JComboBox<String> dayCombo, monthCombo, yearCombo;
    private JComboBox<String> startDayCombo, startMonthCombo, startYearCombo;
    private JRadioButton maleRadio, femaleRadio, otherRadio;
    private JTextField referralSourceField, personalTrainerField;
    private JRadioButton regularRadio, premiumRadio;
    private JComboBox<String> planCombo;
    private JTextField planCharge,removal;
    private JTextField actionIdField;
    private boolean firstTimeSave = true;

    private JButton addButton, displayButton;
    private JButton markAttendanceButton, activateButton, deactivateButton, discountButton;
    private JButton payDueButton, upgradePlanButton, revertpButton, revertrButton, clearButton;
    private JButton saveButton, readButton;

    public GymMemberGUI() {
        setTitle("VR Fitness Studio");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createRegistrationPanel(), BorderLayout.NORTH);
        mainPanel.add(createButtonPanel(), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JScrollPane(displayArea = new JTextArea()), BorderLayout.CENTER);
        
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomButtonPanel.add(clearButton = new JButton("Clear All Fields"));
        bottomButtonPanel.add(saveButton = new JButton("Save to File"));
        bottomButtonPanel.add(readButton = new JButton("Read from File"));
        
        bottomPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        displayArea.setEditable(false);
        add(mainPanel);
        setupEventHandlers();
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.setBorder(BorderFactory.createTitledBorder("Registration Form"));
        panel.setBackground(Color.LIGHT_GRAY);
        
        regularRadio = new JRadioButton("Regular", true);
        premiumRadio = new JRadioButton("Premium");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(regularRadio);
        typeGroup.add(premiumRadio);

        String[] days = getDays(), months = getMonths(), years = getYears();
        String[] plans = {"Basic", "Standard", "Deluxe"};
        planCombo = new JComboBox<>(plans);
        planCharge = new JTextField();

        addComponent(panel, new JLabel("Member Type:"), gbc, 0, 0);
        JPanel typePanel = new JPanel();
        typePanel.add(regularRadio);
        typePanel.add(premiumRadio);
        addComponent(panel, typePanel, gbc, 1, 0);

        addField(panel, "Member ID:", idField = new JTextField(15), gbc, 0, 1);
        addField(panel, "Name:", nameField = new JTextField(15), gbc, 0, 2);
        addDateField(panel, "Date of Birth:", days, months, years, gbc, 3, true);
        addField(panel, "Location:", locationField = new JTextField(15), gbc, 0, 4);
        addField(panel, "Phone:", phoneField = new JTextField(15), gbc, 0, 5);
        addField(panel, "Email:", emailField = new JTextField(15), gbc, 0, 6);
        addGenderField(panel, gbc, 7);
        addDateField(panel, "Membership Start:", days, months, years, gbc, 8, false);
        addField(panel, "Referral Source:", referralSourceField = new JTextField(15), gbc, 0, 9);
        addField(panel, "Personal Trainer:", personalTrainerField = new JTextField(15), gbc, 0, 10);
        addField(panel, "Plan charge", planCharge = new JTextField(15), gbc, 0, 12);
        planCharge.setEditable(false);
        addField(panel, "Removal reason", removal = new JTextField(15), gbc, 0, 13);
        addComponent(panel, new JLabel("Plan:"), gbc, 0, 11);
        addComponent(panel, planCombo, gbc, 1, 11);

        planCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String price = String.valueOf(RegularMember.getPlanPrice((String) planCombo.getSelectedItem()));
                planCharge.setText(price);
            }
        });

        addButton = new JButton("Add Member");
        addComponent(panel, addButton, gbc, 1, 14);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Actions"));

        panel.add(new JLabel("Member ID:"));
        actionIdField = new JTextField(10);
        panel.add(actionIdField);

        displayButton = new JButton("Display Member");
        markAttendanceButton = new JButton("Mark Attendance");
        activateButton = new JButton("Activate Membership");
        deactivateButton = new JButton("Deactivate Membership");
        payDueButton = new JButton("Pay Due Amount");
        upgradePlanButton = new JButton("Upgrade Plan");
        revertpButton = new JButton("Revert Premium Plan");
        revertrButton = new JButton("Revert Regular Plan");
        discountButton = new JButton("Calculate Discount");
        panel.add(displayButton);
        panel.add(markAttendanceButton);
        panel.add(activateButton);
        panel.add(deactivateButton);
        panel.add(payDueButton);
        panel.add(upgradePlanButton);
        panel.add(revertpButton);
        panel.add(revertrButton);
        panel.add(discountButton);

        return panel;
    }

    private void setupEventHandlers() {
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        });

        regularRadio.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        toggleMemberFields(true);
        revertrButton.setEnabled(true);
        revertpButton.setEnabled(false);

        // Set default price for selected plan (Basic by default)
        String selectedPlan = (String) planCombo.getSelectedItem();
        String price = String.valueOf(RegularMember.getPlanPrice(selectedPlan));
        planCharge.setText(price);
        planCharge.setEditable(false);

        // Disable Premium-only buttons
        payDueButton.setEnabled(false);
        discountButton.setEnabled(false);
    }
});

premiumRadio.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        toggleMemberFields(false);
        revertpButton.setEnabled(true);
        revertrButton.setEnabled(false);

        planCharge.setText("50000");
        planCharge.setEditable(false);

        // Enable Premium-only buttons
        payDueButton.setEnabled(true);
        discountButton.setEnabled(true);
    }
});
         toggleMemberFields(true);
        revertrButton.setEnabled(true);
        revertpButton.setEnabled(false);
        
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayMembers();
            }
        });

        markAttendanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markAttendance();
            }
        });

        activateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleMembership(true);
            }
        });

        deactivateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleMembership(false);
            }
        });

        payDueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                payDueAmount();
            }
        });

        upgradePlanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                upgradePlan();
            }
        });

       revertpButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        GymMember member = findMemberByActionField();
        if (member instanceof PremiumMember) {
            int confirm = JOptionPane.showConfirmDialog(
                GymMemberGUI.this,
                "Convert Premium member '" + member.getName() + "' to Regular member?",
                "Confirm Conversion",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                PremiumMember premiumMember = (PremiumMember) member;
                premiumMember.revertPremiumMember();
                members.remove(premiumMember);
                
                RegularMember regularMember = new RegularMember(
                    premiumMember.getID(),
                    premiumMember.getName(),
                    premiumMember.getLocation(),
                    premiumMember.getPhone(),
                    premiumMember.getEmail(),
                    premiumMember.getGender(),
                    premiumMember.getDOB(),
                    premiumMember.getMembershipStartDate(),
                    "Converted from Premium"
                );
                members.add(regularMember);
                
                JOptionPane.showMessageDialog(GymMemberGUI.this, 
                    "Premium Member '" + premiumMember.getName() + 
                    "' converted to Regular Member successfully",
                    "Conversion Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                
                displayMembers(); // Refresh display
            }
        } else {
            JOptionPane.showMessageDialog(GymMemberGUI.this, 
                "Only Premium members can be converted to Regular members",
                "Invalid Operation",
                JOptionPane.WARNING_MESSAGE);
        }
    }
});             //////premionmqo
    revertrButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String idText = actionIdField.getText().trim();

        // Check if Member ID is entered
        if (idText.isEmpty()) {
            showError("Please enter a Member ID to revert.");
            return;
        }

        GymMember member = findMemberByActionField();

        // Proceed only if the member is a RegularMember
        if (member instanceof RegularMember) {
            RegularMember regularMember = (RegularMember) member;

            String removalReason = JOptionPane.showInputDialog(
                GymMemberGUI.this,
                "Enter reason for reverting this member:",
                "Revert Regular Member",
                JOptionPane.PLAIN_MESSAGE
            );

            if (removalReason != null && !removalReason.isEmpty()) {
                regularMember.revertRegularMember(removalReason);
                JOptionPane.showMessageDialog(
                    GymMemberGUI.this,
                    "Member successfully reverted.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else if (removalReason != null) {
                JOptionPane.showMessageDialog(
                    GymMemberGUI.this,
                    "Please input the removal reason.",
                    "Missing Input",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }
});
discountButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String idText = actionIdField.getText();

        if (idText.equals("")) {
            showError("Please enter a Member ID to calculate discount.");
            return;
        }

        GymMember member = findMemberByActionField();
        if (member instanceof PremiumMember) {
            PremiumMember premiumMember = (PremiumMember) member;

            if (!premiumMember.isFullPayment()) {
                JOptionPane.showMessageDialog(
                    GymMemberGUI.this,
                    "Full payment has not been made yet. No discount available.",
                    "Discount Not Available",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
                }

            premiumMember.calculateDiscount(); // Recalculate in case it's not updated
            double discount = premiumMember.getDiscountAmount();
            double netPaid = premiumMember.getNetAmountPaid();

            JOptionPane.showMessageDialog(
                GymMemberGUI.this,
                "Congratulations! You have received a 10% discount.\n" +
                "Discount Amount: NPR " + discount + "\n" +
                "Net Amount Paid: NPR " + netPaid,
                "Discount Granted",
                JOptionPane.INFORMATION_MESSAGE
            );

            displayArea.append(
                "10% discount applied for " + premiumMember.getName() + "\n" +
                "Discount Amount: NPR " + discount + "\n" +
                "Net Amount Paid: NPR " + netPaid + "\n\n"
            );
        }
    }
});


 clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearAllFields();
            }
        });
        
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });
        
        readButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readFromFile();
            }
        });
    }

    private void saveToFile() {
        if (members.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No members to save");
            return;
        }
        
        File packageDir = new File("GYMGUI");
        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }
        
        File outputFile = new File(packageDir, "MemberDetails.txt");
        
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile, !firstTimeSave));
            if (firstTimeSave || outputFile.length() == 0) {
                writer.write(getHeader());
                firstTimeSave = false;
            }
            
            for (GymMember member : members) {
                writer.write(toFormattedString(member));
            }
            
            JOptionPane.showMessageDialog(this, 
                "Saved " + members.size() + " members to " + outputFile.getAbsolutePath(),
                "Save Successful",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving to file: " + e.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readFromFile() {
    File inputFile = new File("GYMGUI/MemberDetails.txt");

    if (!inputFile.exists() || inputFile.length() == 0) {
        JOptionPane.showMessageDialog(this, 
            "No member data found at " + inputFile.getAbsolutePath(),
            "File Not Found",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    JFrame viewFrame = new JFrame("View Members");
    viewFrame.setSize(800, 500);
    viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JTextArea textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));  // Set monospace font

    JScrollPane scrollPane = new JScrollPane(textArea);
    viewFrame.add(scrollPane);

    BufferedReader reader = null;
    try {
        reader = new BufferedReader(new FileReader(inputFile));
        String line;
        while ((line = reader.readLine()) != null) {
            textArea.append(line + "\n");
        }
        viewFrame.setVisible(true);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, 
            "Error reading file: " + e.getMessage(),
            "Read Error",
            JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (reader != null) reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

private String toFormattedString(GymMember m) {
    if (m instanceof RegularMember) {
        RegularMember r = (RegularMember) m;
        return String.format(
            "%-5s %-15s %-15s %-15s %-25s %-18s %-10s %-8.1f %-8s %-10.1f %-8s %-10s %-10.1f %-10.1f\n",
            m.getID(),
            m.getName(),
            m.getLocation(),
            m.getPhone(),
            m.getEmail(),
            m.getMembershipStartDate(),
            r.getPlan(),
            r.getPrice(),
            m.getAttendanceCount(),
            r.getLoyaltyPoints(),
            m.getActiveStatus() ? "Active" : "Inactive",
            r.getFullPayment(),
            r.getDiscountAmount(),
            r.getNetAmountPaid()
        );
    } else if (m instanceof PremiumMember) {
        PremiumMember p = (PremiumMember) m;
        return String.format(
            "%-5s %-15s %-15s %-15s %-25s %-18s %-10s %-8.1f %-8s %-10.1f %-8s %-10s %-10.1f %-10.1f\n",
            m.getID(),
            m.getName(),
            m.getLocation(),
            m.getPhone(),
            m.getEmail(),
            m.getMembershipStartDate(),
            "Premium",
            p.getPremiumCharge(),
            m.getAttendanceCount(),
            p.getLoyaltyPoints(),
            m.getActiveStatus() ? "Active" : "Inactive",
            p.getFullPayment(),
            p.getDiscountAmount(),
            p.getNetAmountPaid()
        );
    }
    return "";
}
private String getHeader() {
    return String.format(
        "%-5s %-15s %-15s %-15s %-25s %-18s %-10s %-8s %-8s %-10s %-8s %-10s %-10s %-10s\n",
        "ID", "Name", "Location", "Phone", "Email", "Membership Start",
        "Plan", "Price", "Attend", "Loyalty", "Status", "Full Pay",
        "Discount", "Net Paid"
    );
}

    private void addMember() {
        try {
            // Validate all fields are filled
            if (idField.getText().isEmpty() || nameField.getText().isEmpty() || 
                locationField.getText().isEmpty() || phoneField.getText().isEmpty() || 
                emailField.getText().isEmpty() || (regularRadio.isSelected() && referralSourceField.getText().isEmpty()) || 
                (premiumRadio.isSelected() && personalTrainerField.getText().isEmpty())) {
                throw new Exception("Please input the valid details ");
            }

            int id = Integer.parseInt(idField.getText());
            if (isDuplicateId(id)) {
                showError("Member ID already exists!");
                return;
            }

            String name = nameField.getText();
            String dob = getDate(dayCombo, monthCombo, yearCombo);
            String startDate = getDate(startDayCombo, startMonthCombo, startYearCombo);

            if (regularRadio.isSelected()) {
                RegularMember member = new RegularMember(
                        id, name, locationField.getText(), phoneField.getText(),
                        emailField.getText(), getGender(), dob, startDate,
                        referralSourceField.getText()
                );
                members.add(member);
            } else {
                PremiumMember member = new PremiumMember(
                        id, name, locationField.getText(), phoneField.getText(),
                        emailField.getText(), getGender(), dob, startDate,
                        personalTrainerField.getText()
                );
                members.add(member);
            }
            JOptionPane.showMessageDialog(this, "Member added successfully!");
            clearAllFields();
        } catch (NumberFormatException ex) {
            showError("Enter Valid ID!");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private boolean isDuplicateId(int id) {
        for (GymMember member : members) {
            if (member.getID() == id) {
                return true;
            }
        }
        return false;
    }
private void displayMembers() {
    String inputId = actionIdField.getText().trim();

    if (inputId.isEmpty()) {
        String all = "";
        for (GymMember member : members) {
            String m = "ID: " + member.getID() + "\n"
                     + "Name: " + member.getName() + "\n"
                     + "Type: " + (member instanceof RegularMember ? "Regular" : "Premium") + "\n";

            if (member instanceof RegularMember) {
                RegularMember r = (RegularMember) member;
                m += "Plan: " + r.getPlan() + "\n"
                   + "Discount Amount: NPR " + r.getDiscountAmount() + "\n";
            } else if (member instanceof PremiumMember) {
                PremiumMember p = (PremiumMember) member;
                m += "Trainer: " + p.getPersonalTrainer() + "\n"
                   + "Discount Amount: NPR " + p.getDiscountAmount() + "\n"
                   + "Net Amount Paid: NPR " + p.getNetAmountPaid() + "\n";
            }

            m += "Status: " + (member.getActiveStatus() ? "Active" : "Inactive") + "\n"
               + "Attendance Count: " + member.getAttendanceCount() + "\n"
               + "----------------------------\n";

            all += m;
        }
        displayArea.setText(all.isEmpty() ? "No members registered yet." : all);
    } else {
        GymMember member = findMemberByActionField();
        if (member != null) {
            String m = "Member Details\n"
                     + "--------------\n"
                     + "ID: " + member.getID() + "\n"
                     + "Name: " + member.getName() + "\n"
                     + "Type: " + (member instanceof RegularMember ? "Regular" : "Premium") + "\n"
                     + "Date of Birth: " + member.getDOB() + "\n"
                     + "Location: " + member.getLocation() + "\n"
                     + "Phone: " + member.getPhone() + "\n"
                     + "Email: " + member.getEmail() + "\n"
                     + "Gender: " + member.getGender() + "\n"
                     + "Membership Start: " + member.getMembershipStartDate() + "\n";

            if (member instanceof RegularMember) {
                RegularMember r = (RegularMember) member;
                m += "Plan: " + r.getPlan() + "\n"
                   + "Referral Source: " + r.getReferralSource() + "\n"
                   + "Loyalty Points: " + r.getLoyaltyPoints() + "\n"
                   + "Discount Amount: NPR " + r.getDiscountAmount() + "\n";
            } else if (member instanceof PremiumMember) {
                PremiumMember p = (PremiumMember) member;
                m += "Personal Trainer: " + p.getPersonalTrainer() + "\n"
                   + "Loyalty Points: " + p.getLoyaltyPoints() + "\n"
                   + "Discount Amount: NPR " + p.getDiscountAmount() + "\n"
                   + "Net Amount Paid: NPR " + p.getNetAmountPaid() + "\n";
            }

            m += "Status: " + (member.getActiveStatus() ? "Active" : "Inactive") + "\n"
               + "Attendance Count: " + member.getAttendanceCount();

            JTextArea textArea = new JTextArea(m);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Member Details", JOptionPane.INFORMATION_MESSAGE);
            displayArea.setText(m);
        }
    }
}


    private GymMember findMemberByActionField() {
        try {
            int id = Integer.parseInt(actionIdField.getText());
            for (GymMember m : members) {
                if (m.getID() == id) return m;
            }
            showError("Member not found!");
        } catch (NumberFormatException ex) {
            showError("ENTER VALID ID!");
        }
        return null;
    }

    private void markAttendance() {
        GymMember member = findMemberByActionField();
        if (member != null) {
            if (member.getActiveStatus()) {
                member.markAttendance();
                JOptionPane.showMessageDialog(this, 
                    "Attendance marked successfully for " + member.getName() + 
                    "\nTotal Attendance: " + member.getAttendanceCount(), 
                    "Attendance Marked", 
                    JOptionPane.INFORMATION_MESSAGE);
                displayArea.append("Attendance marked for " + member.getName() + 
                                 " (Total: " + member.getAttendanceCount() + ")\n");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Cannot mark attendance for inactive member: " + member.getName(),
                    "Membership Inactive",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void toggleMembership(boolean activate) {
    GymMember member = findMemberByActionField();
    if (member != null) {
        if (activate) {
            if (member.getActiveStatus()) {
                JOptionPane.showMessageDialog(this, 
                    member.getName() + "'s membership is already active",
                    "Already Active",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                member.activeMembership();
                JOptionPane.showMessageDialog(this, 
                    "Membership has been successfully activated for " + member.getName(),
                    "Membership Activated", 
                    JOptionPane.INFORMATION_MESSAGE);
                displayArea.append("Membership activated for " + member.getName() + "\n");
            }
        } else {
            if (!member.getActiveStatus()) {
                JOptionPane.showMessageDialog(this, 
                    member.getName() + "'s membership is already inactive",
                    "Already Inactive",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                member.deactivateMembership();
                JOptionPane.showMessageDialog(this, 
                    "Membership has been successfully deactivated for " + member.getName(),
                    "Membership Deactivated", 
                    JOptionPane.INFORMATION_MESSAGE);
                displayArea.append("Membership deactivated for " + member.getName() + "\n");
            }
        }
    }
}

    private void payDueAmount() {
    GymMember member = findMemberByActionField();
    if (member instanceof PremiumMember) {
        PremiumMember premiumMember = (PremiumMember) member;
        double due = premiumMember.getDueAmount();

        String input = JOptionPane.showInputDialog(
            this,
            "Current Due: NPR " + due + "\nEnter amount to pay:",
            "Pay Due Amount",
            JOptionPane.PLAIN_MESSAGE
        );

        if (input != null) {
            try {
                double amount = Double.parseDouble(input);
                String result = premiumMember.payDueAmount(amount);
                displayArea.append(result + "\n");
            } catch (NumberFormatException ex) {
                showError("Invalid amount!");
            }
        }
    } else {
        // No need to show a dialog — button is already disabled for non-premium
    }
}


   private void upgradePlan() {
    GymMember member = findMemberByActionField();
    if (member instanceof RegularMember) {
        RegularMember regularMember = (RegularMember) member;
        
        // Check attendance first
        if (regularMember.getAttendanceCount() < 30) {
            JOptionPane.showMessageDialog(this, 
                "Attendance must be at least 30 to upgrade plan.\nCurrent attendance: " + regularMember.getAttendanceCount(),
                "Upgrade Not Allowed",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create a dialog with plan options
        String[] plans = {"Basic", "Standard", "Deluxe"};
        String selectedPlan = (String) JOptionPane.showInputDialog(
            this,
            "Current Plan: " + regularMember.getPlan() + 
            "\nSelect new plan:",
            "Upgrade Plan",
            JOptionPane.PLAIN_MESSAGE,
            null,
            plans,
            regularMember.getPlan() // Default selection
        );
        
        if (selectedPlan != null) { // User didn't cancel
            if (selectedPlan.equals(regularMember.getPlan())) {
                JOptionPane.showMessageDialog(this,
                    "Member is already on the " + selectedPlan + " plan",
                    "No Change Made",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                String result = regularMember.upgradePlan(selectedPlan);
                JOptionPane.showMessageDialog(this,
                    "Plan changed successfully to " + selectedPlan +
                    "\nNew Price: NPR " + RegularMember.getPlanPrice(selectedPlan),
                    "Plan Upgraded",
                    JOptionPane.INFORMATION_MESSAGE);
                displayArea.append(result + "\n");
            }
        }
    }
}
    private void clearAllFields() {
        idField.setText("");
        nameField.setText("");
        locationField.setText("");
        phoneField.setText("");
        emailField.setText("");
        referralSourceField.setText("");
        personalTrainerField.setText("");
        dayCombo.setSelectedIndex(0);
        monthCombo.setSelectedIndex(0);
        yearCombo.setSelectedIndex(0);
        startDayCombo.setSelectedIndex(0);
        startMonthCombo.setSelectedIndex(0);
        startYearCombo.setSelectedIndex(0);
        maleRadio.setSelected(true);
        regularRadio.setSelected(true);
        planCombo.setSelectedIndex(0);
        planCharge.setText("");
        actionIdField.setText("");
        displayArea.setText("");
    }

    private void toggleMemberFields(boolean isRegular) {
    referralSourceField.setEnabled(isRegular);
    personalTrainerField.setEnabled(!isRegular);
    planCombo.setEnabled(isRegular);
    removal.setEnabled(isRegular); // Disable for premium
}


    private String getGender() {
        if (maleRadio.isSelected()) return "Male";
        if (femaleRadio.isSelected()) return "Female";
        return "Other";
    }

    private String getDate(JComboBox<String> d, JComboBox<String> m, JComboBox<String> y) {
        return d.getSelectedItem() + "/" + m.getSelectedItem() + "/" + y.getSelectedItem();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void addComponent(JPanel p, Component c, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        p.add(c, gbc);
    }

    private void addField(JPanel p, String label, JTextField field, GridBagConstraints gbc, int x, int y) {
        addComponent(p, new JLabel(label), gbc, x, y);
        addComponent(p, field, gbc, x + 1, y);
    }

    private void addDateField(JPanel p, String label, String[] days, String[] months, String[] years,
                              GridBagConstraints gbc, int y, boolean isDOB) {
        addComponent(p, new JLabel(label), gbc, 0, y);
        JPanel datePanel = new JPanel();
        JComboBox<String> day = new JComboBox<>(days);
        JComboBox<String> month = new JComboBox<>(months);
        JComboBox<String> year = new JComboBox<>(years);
        datePanel.add(day);
        datePanel.add(month);
        datePanel.add(year);
        addComponent(p, datePanel, gbc, 1, y);

        if (isDOB) {
            dayCombo = day;
            monthCombo = month;
            yearCombo = year;
        } else {
            startDayCombo = day;
            startMonthCombo = month;
            startYearCombo = year;
        }
    }

    private void addGenderField(JPanel p, GridBagConstraints gbc, int y) {
        addComponent(p, new JLabel("Gender:"), gbc, 0, y);
        JPanel genderPanel = new JPanel();
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        otherRadio = new JRadioButton("Other");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.add(otherRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        genderPanel.add(otherRadio);
        addComponent(p, genderPanel, gbc, 1, y);
    }

    private String[] getDays() {
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) days[i] = String.valueOf(i + 1);
        return days;
    }

    private String[] getMonths() {
        return new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    }

    private String[] getYears() {
        String[] years = new String[35];
        for (int i = 0; i < 35; i++) years[i] = String.valueOf(1995 + i);
        return years;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GymMemberGUI().setVisible(true);
            }
        });
    }
}
