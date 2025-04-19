// Staff.java

// Class representing a staff member
public class Staff {

    // Staff attributes
    int staffID;
    String name;
    String email;
    String phone;
    String position;

    // Constructor to create a new staff object
    public Staff(int staffID, String name, String email, String phone, String position) {
        this.staffID = staffID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = position;
    }

    // Getter and setter for staffID
    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

     // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter and setter for position
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}