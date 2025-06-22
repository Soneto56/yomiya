package com.example.demo.controller;

import com.example.demo.entity.CourseSelection;
import com.example.demo.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courseSelections")
public class CourseSelectionController {

    @Autowired
    private CourseSelectionService courseSelectionService;

    @GetMapping
    public String listCourseSelections(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<CourseSelection> courseSelections = courseSelectionService.getCourseSelectionsByPage(page, size);
        model.addAttribute("courseSelections", courseSelections.getContent());
        model.addAttribute("currentPage", courseSelections.getNumber());
        model.addAttribute("totalPages", courseSelections.getTotalPages());
        return "courseSelection/list";
    }

    @GetMapping("/add")
    public String showAddCourseSelectionForm(Model model) {
        model.addAttribute("courseSelection", new CourseSelection());
        return "courseSelection/add";
    }

    @PostMapping("/add")
    public String addCourseSelection(@ModelAttribute CourseSelection courseSelection) {
        courseSelectionService.saveCourseSelection(courseSelection);
        return "redirect:/courseSelections";
    }

    @GetMapping("/edit/{selectionId}")
    public String showEditCourseSelectionForm(@PathVariable Integer selectionId, Model model) {
        CourseSelection courseSelection = courseSelectionService.getCourseSelectionById(selectionId);
        model.addAttribute("courseSelection", courseSelection);
        return "courseSelection/edit";
    }

    @PostMapping("/edit/{selectionId}")
    public String editCourseSelection(@PathVariable Integer selectionId, @ModelAttribute CourseSelection courseSelection) {
        courseSelection.setSelectionId(selectionId);
        courseSelectionService.saveCourseSelection(courseSelection);
        return "redirect:/courseSelections";
    }

    @GetMapping("/delete/{selectionId}")
    public String deleteCourseSelection(@PathVariable Integer selectionId) {
        courseSelectionService.deleteCourseSelection(selectionId);
        return "redirect:/courseSelections";
    }
}