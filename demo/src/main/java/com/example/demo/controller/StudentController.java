package                                                                                  com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "student/list";
    }

    @GetMapping("/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "student/add";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute Student student) {
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/edit/{studentId}")
    public String showEditStudentForm(@PathVariable String studentId, Model model) {
        Student student = studentService.getStudentById(studentId);
        model.addAttribute("student", student);
        return "student/edit";
    }

    @PostMapping("/edit/{studentId}")
    public String editStudent(@PathVariable String studentId, @ModelAttribute Student student) {
        student.setStudentId(studentId);
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/delete/{studentId}")
    public String deleteStudent(@PathVariable String studentId) {
        studentService.deleteStudent(studentId);
        return "redirect:/students";
    }
}