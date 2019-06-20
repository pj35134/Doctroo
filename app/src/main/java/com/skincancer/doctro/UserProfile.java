package com.skincancer.doctro;

public class UserProfile {

    private String userName;
    private String userEmail;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String gender;


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    String age;
    public UserProfile(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
