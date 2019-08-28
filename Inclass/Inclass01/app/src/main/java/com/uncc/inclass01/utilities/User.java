package com.uncc.inclass01.utilities;

import android.util.Log;

public class User {
    private String firstName, lastName, email, gender, city;

    public User(){

    }

    public User(String firstName, String lastName, String email, String gender, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.city = city;
    }

    public void printData(){
        Log.d("UserClass",firstName+", "+lastName+", email: "+email);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoto() {
        return this.email.replace('.','_')+".jpeg";
    }

}
