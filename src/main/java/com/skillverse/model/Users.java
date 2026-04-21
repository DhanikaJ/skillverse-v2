package com.skillverse.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fname;
    private String lname;
    private String email;
    private String password_hash;
    private String verification;
    private Integer status_id;
    private LocalDateTime created_at;
    private Integer city_id;
    private String photo;
    private Integer gender_id;
    @OneToMany(mappedBy = "users")
    private List<Course> course;

    public Users() {
    }

    public Users(Integer id, String fname, String lname, String email, String password_hash, String verification, Integer status_id, LocalDateTime created_at, Integer city_id, String photo, Integer gender_id) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password_hash = password_hash;
        this.verification = verification;
        this.status_id = status_id;
        this.created_at = created_at;
        this.city_id = city_id;
        this.photo = photo;
        this.gender_id = gender_id;
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

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getGender_id() {
        return gender_id;
    }

    public void setGender_id(Integer gender_id) {
        this.gender_id = gender_id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Users user = (Users) o;
        return Objects.equals(id, user.id) && Objects.equals(fname, user.fname) && Objects.equals(lname, user.lname) && Objects.equals(email, user.email) && Objects.equals(password_hash, user.password_hash) && Objects.equals(verification, user.verification) && Objects.equals(status_id, user.status_id) && Objects.equals(created_at, user.created_at) && Objects.equals(city_id, user.city_id) && Objects.equals(photo, user.photo) && Objects.equals(gender_id, user.gender_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fname, lname, email, password_hash, verification, status_id, created_at, city_id, photo, gender_id);
    }
}
