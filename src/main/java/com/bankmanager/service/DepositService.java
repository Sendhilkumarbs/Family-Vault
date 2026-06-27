package com.bankmanager.service;

import com.bankmanager.model.Deposit;
import com.bankmanager.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositService {

    @Autowired
    private DepositRepository repository;

    public List<Deposit> getAllDeposits() {
        return repository.findAll();
    }

    public Deposit getDepositById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Deposit saveDeposit(Deposit deposit) {
        return repository.save(deposit);
    }

    public void deleteDeposit(Long id) {
        repository.deleteById(id);
    }

    public List<Deposit> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll();
        }
        return repository.searchByKeyword(keyword.trim());
    }

    public long getTotalCount() {
        return repository.count();
    }

    public long getFdCount() {
        return repository.countByDepositType("FD");
    }

    public long getRdCount() {
        return repository.countByDepositType("RD");
    }
}
