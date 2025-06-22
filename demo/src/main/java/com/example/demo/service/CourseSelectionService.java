package com.example.demo.service;

import com.example.demo.entity.CourseSelection;
import com.example.demo.repository.CourseSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseSelectionService {

    @Autowired
    private CourseSelectionRepository courseSelectionRepository;

    public Page<CourseSelection> getCourseSelectionsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("selectionTime").descending());
        return courseSelectionRepository.findAll(pageable);
    }

    public long countCourseSelectionsBySemester(String semester) {
        return courseSelectionRepository.countBySemester(semester);
    }

    public List<CourseSelection> getAllCourseSelections() {
        return courseSelectionRepository.findAll();
    }

    public CourseSelection getCourseSelectionById(Integer selectionId) {
        return courseSelectionRepository.findById(selectionId).orElse(null);
    }

    public CourseSelection saveCourseSelection(CourseSelection courseSelection) {
        return courseSelectionRepository.save(courseSelection);
    }

    public void deleteCourseSelection(Integer selectionId) {
        courseSelectionRepository.deleteById(selectionId);
    }
}