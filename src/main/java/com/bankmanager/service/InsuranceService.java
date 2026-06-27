package com.bankmanager.service;

import com.bankmanager.model.Insurance;
import com.bankmanager.repository.InsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceService {

    @Autowired
    private InsuranceRepository repository;

    public List<Insurance> getAllInsurance() {
        return repository.findAll();
    }

    public Insurance getInsuranceById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Insurance saveInsurance(Insurance insurance) {
        return repository.save(insurance);
    }

    public void deleteInsurance(Long id) {
        repository.deleteById(id);
    }

    public List<Insurance> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll();
        }
        return repository.searchByKeyword(keyword.trim());
    }

    public long getTotalCount() {
        return repository.count();
    }

    public long getCountByType(String insuranceType) {
        return repository.findByInsuranceType(insuranceType).size();
    }
}
