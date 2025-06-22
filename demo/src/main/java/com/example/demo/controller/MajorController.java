package com.example.demo.controller;

import com.example.demo.entity.Major;
import com.example.demo.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
public class MajorController {

    @Autowired
    private MajorService majorService;

    /**
     * 获取所有专业信息
     * @return 专业列表
     */
    @GetMapping
    public ResponseEntity<List<Major>> getAllMajors() {
        List<Major> majors = majorService.getAllMajors();
        return new ResponseEntity<>(majors, HttpStatus.OK);
    }

    /**
     * 根据 ID 获取专业信息
     * @param majorId 专业 ID
     * @return 专业信息，如果不存在则返回 404
     */
    @GetMapping("/{majorId}")
    public ResponseEntity<Major> getMajorById(@PathVariable Integer majorId) {
        Major major = majorService.getMajorById(majorId);
        if (major != null) {
            return new ResponseEntity<>(major, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 保存或更新专业信息
     * @param major 专业实体
     * @return 保存或更新后的专业信息
     */
    @PostMapping
    public ResponseEntity<Major> saveMajor(@RequestBody Major major) {
        Major savedMajor = majorService.saveMajor(major);
        return new ResponseEntity<>(savedMajor, HttpStatus.CREATED);
    }

    /**
     * 根据 ID 删除专业信息
     * @param majorId 专业 ID
     * @return 204 表示删除成功
     */
    @DeleteMapping("/{majorId}")
    public ResponseEntity<Void> deleteMajor(@PathVariable Integer majorId) {
        majorService.deleteMajor(majorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}