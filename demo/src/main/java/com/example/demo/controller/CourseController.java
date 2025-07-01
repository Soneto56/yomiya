package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // 1. 获取所有课程列表
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return ResponseEntity.ok(courses);
    }

    // 2. 根据ID获取单个课程详情
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. 根据课程名称模糊查询
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCoursesByName(
            @RequestParam String name) {
        List<Course> courses = courseRepository.findByCourseNameContainingIgnoreCase(name);
        return ResponseEntity.ok(courses);
    }

    // 4. 根据院系ID查询课程
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Course>> getCoursesByDepartment(
            @PathVariable Long departmentId) {
        List<Course> courses = courseRepository.findByDepartmentId(departmentId);
        return ResponseEntity.ok(courses);
    }

    // 5. 分页查询课程
    @GetMapping("/paged")
    public ResponseEntity<Page<Course>> getCoursesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "courseName") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Course> courses = courseRepository.findAll(pageable);
        return ResponseEntity.ok(courses);
    }

    // 6. 根据学分范围查询课程
    @GetMapping("/credit-range")
    public ResponseEntity<List<Course>> getCoursesByCreditRange(
            @RequestParam BigDecimal minCredit,
            @RequestParam BigDecimal maxCredit) {

        List<Course> courses = courseRepository.findByCreditBetween(minCredit, maxCredit);
        return ResponseEntity.ok(courses);
    }

    // 7. 根据学时查询课程
    @GetMapping("/hours")
    public ResponseEntity<List<Course>> getCoursesByHours(
            @RequestParam Integer hours) {

        List<Course> courses = courseRepository.findByHours(hours);
        return ResponseEntity.ok(courses);
    }
}