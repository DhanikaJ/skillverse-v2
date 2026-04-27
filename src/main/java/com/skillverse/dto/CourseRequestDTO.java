package com.skillverse.dto;

import jakarta.validation.constraints.*;

public class CourseRequestDTO {
    
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;
    
    @NotBlank(message = "Price level cannot be blank")
    private String pricelevel;
    
    @NotBlank(message = "Difficulty level cannot be blank")
    private String difficulty;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price must not exceed 999999.99")
    private double price;
    
    @Size(max = 500, message = "Thumbnail URL must not exceed 500 characters")
    private String thumbnail;
    
    public CourseRequestDTO() {
    }
    
    public CourseRequestDTO(String title, String description, String pricelevel, String difficulty, double price, String thumbnail) {
        this.title = title;
        this.description = description;
        this.pricelevel = pricelevel;
        this.difficulty = difficulty;
        this.price = price;
        this.thumbnail = thumbnail;
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
}

