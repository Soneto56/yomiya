package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody Student student) {
        try {
            // 检查学号是否已存在
            if (studentService.getStudentById(student.getStudentId()).isPresent()) {
                return ResponseEntity.badRequest().body("学号已存在");
            }

            // 检查邮箱是否已存在
            if (studentService.existsByEmail(student.getEmail())) {
                return ResponseEntity.badRequest().body("邮箱已存在");
            }

            // 注册学生
            Student registeredStudent = studentService.registerStudent(student);
            return new ResponseEntity<>(registeredStudent, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("注册失败: " + e.getMessage());
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        try {
            Map<String, Object> result = new HashMap<>();
            boolean exists = studentService.existsByEmail(email);
            result.put("exists", exists);

            if (exists) {
                // 如果邮箱存在，查询对应的学号
                Optional<Student> student = studentService.findByEmail(email);
                student.ifPresent(s -> result.put("studentId", s.getStudentId()));
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // 添加异常处理
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("exists", false);
            errorResult.put("error", "服务器错误，请稍后再试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }
    }

    @GetMapping("/check-student-id")
    public ResponseEntity<Map<String, Object>> checkStudentId(@RequestParam String studentId) {
        try {
            Map<String, Object> result = new HashMap<>();
            boolean exists = studentService.getStudentById(studentId).isPresent();
            result.put("exists", exists);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // 添加异常处理
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("exists", false);
            errorResult.put("error", "服务器错误，请稍后再试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }
    }

    // 添加缺失的登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String studentId = loginRequest.get("studentId");
            if (studentId == null || studentId.isEmpty()) {
                return ResponseEntity.badRequest().body("学号不能为空");
            }

            Optional<Student> studentOptional = studentService.getStudentById(studentId);
            if (studentOptional.isPresent()) {
                // 登录成功，返回学生信息和token（实际项目中应生成JWT）
                Map<String, Object> response = new HashMap<>();
                response.put("message", "登录成功");
                response.put("student", studentOptional.get());
                response.put("token", "generated_token_here");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("学号不存在");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("登录失败: " + e.getMessage());
        }
    }
}