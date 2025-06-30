package com.example.demo.service;

import com.example.demo.entity.Teacher;
import com.example.demo.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(String teacherId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        return optionalTeacher.orElse(null);
    }

    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public void deleteTeacher(String teacherId) {
        teacherRepository.deleteById(teacherId);
    }

    // 新增教师注册方法
    public Teacher registerTeacher(Teacher teacher) {
        return saveTeacher(teacher);
    }
}