package com.example.demo.repository;

import com.example.demo.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> {

    // 根据课程名称模糊查询（不区分大小写）
    List<Course> findByCourseNameContainingIgnoreCase(String name);

    // 根据院系ID查询课程
    @Query("SELECT c FROM Course c WHERE c.department.id = :departmentId")
    List<Course> findByDepartmentId(@Param("departmentId") Long departmentId);

    // 根据学分范围查询课程
    List<Course> findByCreditBetween(BigDecimal minCredit, BigDecimal maxCredit);

    // 根据学时查询课程
    List<Course> findByHours(Integer hours);
}