package com.skillverse.dto;

import jakarta.validation.constraints.*;

public class LessonRequestDTO {

    @NotNull(message = "Course ID cannot be null")
    @Positive(message = "Course ID must be a positive number")
    private Integer courseId;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Video URL cannot be blank")
    @Size(min = 10, max = 500, message = "Video URL must be between 10 and 500 characters")
    private String video_url;

    @NotNull(message = "Order index cannot be null")
    @Min(value = 1, message = "Order index must be at least 1")
    private Integer order_index;

    @Size(max = 500, message = "Resource file path must not exceed 500 characters")
    private String resource_file;

    public LessonRequestDTO() {
    }

    public LessonRequestDTO(Integer courseId, String title, String video_url, Integer order_index, String resource_file) {
        this.courseId = courseId;
        this.title = title;
        this.video_url = video_url;
        this.order_index = order_index;
        this.resource_file = resource_file;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public Integer getOrder_index() {
        return order_index;
    }

    public void setOrder_index(Integer order_index) {
        this.order_index = order_index;
    }

    public String getResource_file() {
        return resource_file;
    }

    public void setResource_file(String resource_file) {
        this.resource_file = resource_file;
    }
}

