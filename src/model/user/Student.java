package model.user;

public class Student extends User {
       
    int sectionKey;
   
    
    public void load(String studentID, String firstName, String middleName, String lastName, int sectionKey, String password) {
        this.userID = studentID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.password = password;
        this.sectionKey = sectionKey;
    }


    public String getStudentID() {
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

    public int getSectionKey() {
        return sectionKey;
    }
    
    public String getPassword() {
        return password;
    }

}
