package com.bankmanager.repository;

import com.bankmanager.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    List<CreditCard> findByBankAccountId(Long bankAccountId);

    void deleteByBankAccountId(Long bankAccountId);
}
