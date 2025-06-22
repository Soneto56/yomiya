package com.example.demo.controller;

import com.example.demo.entity.Teacher;
import com.example.demo.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public String listTeachers(Model model) {
        List<Teacher> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        return "teacher/list";
    }

    @GetMapping("/add")
    public String showAddTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "teacher/add";
    }

    @PostMapping("/add")
    public String addTeacher(@ModelAttribute Teacher teacher) {
        teacherService.saveTeacher(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/edit/{teacherId}")
    public String showEditTeacherForm(@PathVariable String teacherId, Model model) {
        Teacher teacher = teacherService.getTeacherById(teacherId);
        model.addAttribute("teacher", teacher);
        return "teacher/edit";
    }

    @PostMapping("/edit/{teacherId}")
    public String editTeacher(@PathVariable String teacherId, @ModelAttribute Teacher teacher) {
        teacher.setTeacherId(teacherId);
        teacherService.saveTeacher(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/delete/{teacherId}")
    public String deleteTeacher(@PathVariable String teacherId) {
        teacherService.deleteTeacher(teacherId);
        return "redirect:/teachers";
    }
}