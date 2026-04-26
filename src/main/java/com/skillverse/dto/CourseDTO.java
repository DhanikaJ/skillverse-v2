package com.skillverse.dto;

import jakarta.validation.constraints.*;
import java.util.Date;

public class CourseDTO {
    private Integer id;

    @NotBlank(message = "Course title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    @Size(max = 50, message = "Price level must not exceed 50 characters")
    private String pricelevel;

    @Size(max = 50, message = "Difficulty must not exceed 50 characters")
    private String difficulty;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    @DecimalMax(value = "999999.99", message = "Price must not exceed 999999.99")
    private double price;

    @Size(max = 500, message = "Thumbnail URL must not exceed 500 characters")
    private String thumbnail;

    private Date created_at;

    public CourseDTO() {
    }

    public CourseDTO(Integer id, String title, String description, String pricelevel, String difficulty, double price, String thumbnail, Date created_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pricelevel = pricelevel;
        this.difficulty = difficulty;
        this.price = price;
        this.thumbnail = thumbnail;
        this.created_at = created_at;
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
}


