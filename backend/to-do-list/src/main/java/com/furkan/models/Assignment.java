package com.furkan.models;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name="assignment")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description", nullable = false)
    @NotNull
    private String description;

    @Column(name = "status", nullable = false)
    @NotNull
    private String status;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Integer userId;

    public Assignment(String description, String status, Integer userId) {
        this.description = description;
        this.status = status;
        this.userId = userId;
    }

    public Assignment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer user_id) {
        this.userId = user_id;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", user_id='" + userId + '\'' +
                '}';
    }
}

