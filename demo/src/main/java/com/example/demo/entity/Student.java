package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Student {
    @Id
    private String studentId;
    private String name;
    private String gender;
    private Date birthDate;
    private Date enrollmentDate;
    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;
    private String classInfo;
    private String phone;
    private String email;
    private String address;
}