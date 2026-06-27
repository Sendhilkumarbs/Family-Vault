package com.bankmanager.repository;

import com.bankmanager.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    @Query("SELECT d FROM Deposit d WHERE " +
           "LOWER(d.personName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.bankName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.depositAccountNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.depositType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.relationship) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Deposit> searchByKeyword(@Param("keyword") String keyword);

    List<Deposit> findByDepositType(String depositType);

    long countByDepositType(String depositType);
}
