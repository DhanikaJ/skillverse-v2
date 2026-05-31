package com.skillverse.dto;

import jakarta.validation.constraints.*;

public class CategoryDTO {
    private Integer id;

    @NotBlank(message = "Category type cannot be blank")
    @Size(min = 2, max = 100, message = "Category type must be between 2 and 100 characters")
    private String type;

    public CategoryDTO() {
    }

    public CategoryDTO(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

