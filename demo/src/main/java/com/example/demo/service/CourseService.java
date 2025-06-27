package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // 原有基础方法
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(String courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        return optionalCourse.orElse(null);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(String courseId) {
        courseRepository.deleteById(courseId);
    }

    // 新增分页查询方法
    public Page<Course> getCoursesByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findAll(pageable);
    }

    // 新增按院系查询方法
    public List<Course> getCoursesByDepartment(Integer departmentId) {
        return courseRepository.findByDepartment_DepartmentId(departmentId);
    }

    // 新增课程搜索方法
    public List<Course> searchCourses(String keyword) {
        return courseRepository.findByCourseNameContainingIgnoreCase(keyword);
    }
}