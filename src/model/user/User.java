package model.user;

public class User {
    
    String userID;
    String firstName;
    String middleName;
    String lastName;
    String password;
    String userType;

    public void setID(String userID) {
        this.userID = userID;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }    

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserID() {
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
