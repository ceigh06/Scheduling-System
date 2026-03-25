package model.user;

import java.util.List;

import model.Course;

public class Student extends User {
       
    int sectionKey;
    List<Course> courses;
    
    public void load(String studentID, String firstName, String middleName, String lastName, int sectionKey, String password) {
        this.userID = studentID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.password = password;
        this.sectionKey = sectionKey;
    }

    public List<Course> getCourses(){
        return courses;
    }

    public void loadCourses(List<Course> courses){
        this.courses = courses;
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
