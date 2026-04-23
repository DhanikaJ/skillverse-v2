package com.skillverse.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
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
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City  city;
    private String photo;
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;
    @OneToMany(mappedBy = "users")
    private List<Course> course;
    @OneToMany(mappedBy = "user")
    private List<Enrollment> enrollments;

    public Users() {
    }

    public Users(Integer id, String fname, String lname, String email, String password_hash, String verification, Status status, Date created_at, City city, String photo, Gender gender, List<Course> course) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password_hash = password_hash;
        this.verification = verification;
        this.status = status;
        this.created_at = created_at;
        this.city = city;
        this.photo = photo;
        this.gender = gender;
        this.course = course;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Users user = (Users) o;
        return Objects.equals(id, user.id) && Objects.equals(fname, user.fname) && Objects.equals(lname, user.lname) && Objects.equals(email, user.email) && Objects.equals(password_hash, user.password_hash) && Objects.equals(verification, user.verification) && Objects.equals(status, user.status) && Objects.equals(created_at, user.created_at) && Objects.equals(city, user.city) && Objects.equals(photo, user.photo) && Objects.equals(gender, user.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fname, lname, email, password_hash, verification, status, created_at, city, photo, gender);
    }
}
