package com.example.demo.repository;

import com.example.demo.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    // 按院系查询
    List<Course> findByDepartment_DepartmentId(Integer departmentId);

    // 课程名称搜索（不区分大小写）
    List<Course> findByCourseNameContainingIgnoreCase(String keyword);

    // 分页查询已由JpaRepository的findAll(Pageable)方法提供
}