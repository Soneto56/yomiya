package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 获取所有课程列表
     * @return 课程列表
     */
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    /**
     * 分页查询课程
     * @param page 页码(从0开始)
     * @param size 每页数量
     * @return 分页课程数据
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Course>> getCoursesByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Course> coursePage = courseService.getCoursesByPage(page, size);
        return new ResponseEntity<>(coursePage, HttpStatus.OK);
    }

    /**
     * 按院系查询课程
     * @param departmentId 院系ID
     * @return 课程列表
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Course>> getCoursesByDepartment(
            @PathVariable Integer departmentId) {
        List<Course> courses = courseService.getCoursesByDepartment(departmentId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    /**
     * 按课程ID查询课程详情
     * @param courseId 课程ID
     * @return 课程详情
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(
            @PathVariable String courseId) {
        Course course = courseService.getCourseById(courseId);
        if (course != null) {
            return new ResponseEntity<>(course, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 添加新课程
     * @param course 课程对象
     * @return 新增的课程
     */
    @PostMapping
    public ResponseEntity<Course> addCourse(
            @RequestBody Course course) {
        Course savedCourse = courseService.saveCourse(course);
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
    }

    /**
     * 更新课程信息
     * @param courseId 课程ID
     * @param course 课程对象
     * @return 更新后的课程
     */
    @PutMapping("/{courseId}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable String courseId,
            @RequestBody Course course) {
        course.setCourseId(courseId);
        Course updatedCourse = courseService.saveCourse(course);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    /**
     * 删除课程
     * @param courseId 课程ID
     * @return 空响应
     */
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 按课程名称搜索课程
     * @param keyword 搜索关键词
     * @return 匹配的课程列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(
            @RequestParam String keyword) {
        List<Course> courses = courseService.searchCourses(keyword);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }
}