package com.bankmanager.service;

import com.bankmanager.model.BankAccount;
import com.bankmanager.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository repository;

    public List<BankAccount> getAllAccounts() {
        return repository.findAll();
    }

    public BankAccount getAccountById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public BankAccount saveAccount(BankAccount account) {
        return repository.save(account);
    }

    public void deleteAccount(Long id) {
        repository.deleteById(id);
    }

    public List<BankAccount> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll();
        }
        return repository.searchByKeyword(keyword.trim());
    }

    public long getTotalCount() {
        return repository.count();
    }
}
