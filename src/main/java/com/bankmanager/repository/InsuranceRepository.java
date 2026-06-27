package com.bankmanager.repository;

import com.bankmanager.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

    @Query("SELECT i FROM Insurance i WHERE " +
           "LOWER(i.personName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.insuranceCompany) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.policyNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.insuranceType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.policyName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Insurance> searchByKeyword(@Param("keyword") String keyword);

    List<Insurance> findByInsuranceType(String insuranceType);
}
