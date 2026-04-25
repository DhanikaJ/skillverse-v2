package com.skillverse.dto;

import java.util.Date;

public class UserDTO {
    private Integer id;
    private String fname;
    private String lname;
    private String email;
    private String phone;
    private Date created_at;

    public UserDTO() {
    }

    public UserDTO(Integer id, String fname, String lname, String email, String phone, Date created_at) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}


