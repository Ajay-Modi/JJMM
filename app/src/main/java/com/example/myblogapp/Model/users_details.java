package com.example.myblogapp.Model;

public class users_details {

    private int id;
    private String username;
    private String email;
    private String password;
    private String full_name;
    private int gender;
    private String phone_contact;
    private String profile_image_address;
    private String profile_image_base64;

    public String getProfile_image_base64() {
        return profile_image_base64;
    }

    public void setProfile_image_base64(String profile_image_base64) {
        this.profile_image_base64 = profile_image_base64;
    }

    public String getProfile_image_address() {
        return profile_image_address;
    }

    public void setProfile_image_address(String profile_image_address) {
        this.profile_image_address = profile_image_address;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone_contact() {
        return phone_contact;
    }

    public void setPhone_contact(String phone_contact) {
        this.phone_contact = phone_contact;
    }

}
