package com.skillverse.dto;

import java.util.Date;

public class CourseDTO {
    private Integer id;
    private String title;
    private String description;
    private String pricelevel;
    private String difficulty;
    private CategoryDTO category;
    private double price;
    private String thumbnail;
    private Date created_at;
    private StatusDTO status;
    private UserDTO users;

    public CourseDTO() {
    }

    public CourseDTO(Integer id, String title, String description, String pricelevel, String difficulty, CategoryDTO category, double price, String thumbnail, Date created_at, StatusDTO status, UserDTO users) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pricelevel = pricelevel;
        this.difficulty = difficulty;
        this.category = category;
        this.price = price;
        this.thumbnail = thumbnail;
        this.created_at = created_at;
        this.status = status;
        this.users = users;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPricelevel() {
        return pricelevel;
    }

    public void setPricelevel(String pricelevel) {
        this.pricelevel = pricelevel;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    public UserDTO getUsers() {
        return users;
    }

    public void setUsers(UserDTO users) {
        this.users = users;
    }
}


