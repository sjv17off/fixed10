package com.example.tasko.service;

import com.example.tasko.model.Enterprise;
import com.example.tasko.repository.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnterpriseService {

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Transactional(readOnly = true)
    public List<Enterprise> getAllEnterprises() {
        return enterpriseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Enterprise getEnterpriseById(Long id) {
        return enterpriseRepository.findById(id).orElse(null);
    }

    @Transactional
    public Enterprise createEnterprise(Enterprise enterprise) {
        return enterpriseRepository.save(enterprise);
    }

    @Transactional
    public Enterprise updateEnterprise(Enterprise enterprise) {
        return enterpriseRepository.save(enterprise);
    }

    @Transactional
    public void deleteEnterprise(Long id) {
        enterpriseRepository.deleteById(id);
    }
}