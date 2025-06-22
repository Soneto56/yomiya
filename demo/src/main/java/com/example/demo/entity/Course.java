package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Course {
    @Id
    private String courseId;
    private String courseName;
    private BigDecimal credit;
    private Integer hours;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private com.example.demo.entity.Department department;
    private String description;
}