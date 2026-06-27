package com.bankmanager.repository;

import com.bankmanager.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query("SELECT b FROM BankAccount b WHERE " +
           "LOWER(b.personName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.bankName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.accountNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.accountType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.relationship) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<BankAccount> searchByKeyword(@Param("keyword") String keyword);

    List<BankAccount> findByAccountType(String accountType);

    List<BankAccount> findByPersonNameContainingIgnoreCase(String personName);
}
