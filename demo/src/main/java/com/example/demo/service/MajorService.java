package com.example.demo.service;

import com.example.demo.entity.Major;
import com.example.demo.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MajorService {

    @Autowired
    private MajorRepository majorRepository;

    /**
     * 获取所有专业信息
     * @return 专业列表
     */
    public List<Major> getAllMajors() {
        return majorRepository.findAll();
    }

    /**
     * 根据 ID 获取专业信息
     * @param majorId 专业 ID
     * @return 专业信息，如果不存在则返回 null
     */
    public Major getMajorById(Integer majorId) {
        Optional<Major> optionalMajor = majorRepository.findById(majorId);
        return optionalMajor.orElse(null);
    }

    /**
     * 保存或更新专业信息
     * @param major 专业实体
     * @return 保存或更新后的专业信息
     */
    public Major saveMajor(Major major) {
        return majorRepository.save(major);
    }

    /**
     * 根据 ID 删除专业信息
     * @param majorId 专业 ID
     */
    public void deleteMajor(Integer majorId) {
        majorRepository.deleteById(majorId);
    }
}