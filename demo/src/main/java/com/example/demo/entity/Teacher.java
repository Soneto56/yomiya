package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Teacher {
    @Id
    private String teacherId;
    private String name;
    private String gender;
    private Date birthDate;
    private Date hireDate;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private com.example.demo.entity.Department department;
    private String title;
    private String phone;
    private String email;
}