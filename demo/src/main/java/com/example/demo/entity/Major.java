package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer majorId;
    private String majorName;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private com.example.demo.entity.Department department;
    private String degreeType;
    private Integer duration;
}