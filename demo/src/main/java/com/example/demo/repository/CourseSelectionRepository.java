package com.example.demo.repository;

import com.example.demo.entity.CourseSelection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseSelectionRepository extends JpaRepository<CourseSelection, Integer> {
    Page<CourseSelection> findAll(Pageable pageable);
    long countBySemester(String semester);
}