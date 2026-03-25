package model.user;

public class Faculty extends User {

    private String position;
    private String collegeCode;

    public void load(String employeeID, String firstName, String middleName, String lastName, String position,String collegeCode, String password) {
        this.userID = employeeID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.position = position;
        this.collegeCode = collegeCode;
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public String getCollegeCode() {
        return collegeCode;
    }

    public String getFacultyID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}
