package com.bankmanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ─── Person Details ──────────────────────────────────────
    private String personName;
    private String relationship; // Self, Spouse, Father, Mother, Son, Daughter, Brother, Sister, Other

    // ─── Bank Account Details ────────────────────────────────
    private String bankName;
    private String branchName;
    private String customerId;
    private String accountNumber;
    private String accountType; // Savings, Current, Joint
    private String ifscCode;
    private String micrCode;

    // ─── Joint Account Details ───────────────────────────────
    private String jointHolderName;
    private String jointHolderRelationship;

    // ─── Net Banking Details ─────────────────────────────────
    private String netBankingUserId;
    private String netBankingPassword;
    private String transactionPassword;

    // ─── Mobile Banking Details ──────────────────────────────
    private String registeredMobileNumber;
    private String mobileBankingPin;
    private String upiId;

    // ─── Debit Card Details ──────────────────────────────────
    private String debitCardNumber;
    private String debitCardPin;
    private String debitCardExpiryDate;
    private String debitCardCvv;

    // ─── Credit Card Details ─────────────────────────────────
    private String creditCardNumber;
    private String creditCardPin;
    private String creditCardExpiryDate;
    private String creditCardCvv;
    private String creditCardLimit;

    // ─── Extra Credit Cards ──────────────────────────────────
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CreditCard> extraCreditCards = new ArrayList<>();

    // ─── Additional Details ──────────────────────────────────
    private String nomineeName;
    private String nomineeRelationship;
    private String emailLinked;

    @Column(length = 1000)
    private String remarks;

    // ─── Timestamps ──────────────────────────────────────────
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ─── Constructors ────────────────────────────────────────
    public BankAccount() {
    }

    // ─── Getters and Setters ─────────────────────────────────

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getMicrCode() {
        return micrCode;
    }

    public void setMicrCode(String micrCode) {
        this.micrCode = micrCode;
    }

    public String getJointHolderName() {
        return jointHolderName;
    }

    public void setJointHolderName(String jointHolderName) {
        this.jointHolderName = jointHolderName;
    }

    public String getJointHolderRelationship() {
        return jointHolderRelationship;
    }

    public void setJointHolderRelationship(String jointHolderRelationship) {
        this.jointHolderRelationship = jointHolderRelationship;
    }

    public String getNetBankingUserId() {
        return netBankingUserId;
    }

    public void setNetBankingUserId(String netBankingUserId) {
        this.netBankingUserId = netBankingUserId;
    }

    public String getNetBankingPassword() {
        return netBankingPassword;
    }

    public void setNetBankingPassword(String netBankingPassword) {
        this.netBankingPassword = netBankingPassword;
    }

    public String getTransactionPassword() {
        return transactionPassword;
    }

    public void setTransactionPassword(String transactionPassword) {
        this.transactionPassword = transactionPassword;
    }

    public String getRegisteredMobileNumber() {
        return registeredMobileNumber;
    }

    public void setRegisteredMobileNumber(String registeredMobileNumber) {
        this.registeredMobileNumber = registeredMobileNumber;
    }

    public String getMobileBankingPin() {
        return mobileBankingPin;
    }

    public void setMobileBankingPin(String mobileBankingPin) {
        this.mobileBankingPin = mobileBankingPin;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getDebitCardNumber() {
        return debitCardNumber;
    }

    public void setDebitCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    public String getDebitCardPin() {
        return debitCardPin;
    }

    public void setDebitCardPin(String debitCardPin) {
        this.debitCardPin = debitCardPin;
    }

    public String getDebitCardExpiryDate() {
        return debitCardExpiryDate;
    }

    public void setDebitCardExpiryDate(String debitCardExpiryDate) {
        this.debitCardExpiryDate = debitCardExpiryDate;
    }

    public String getDebitCardCvv() {
        return debitCardCvv;
    }

    public void setDebitCardCvv(String debitCardCvv) {
        this.debitCardCvv = debitCardCvv;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardPin() {
        return creditCardPin;
    }

    public void setCreditCardPin(String creditCardPin) {
        this.creditCardPin = creditCardPin;
    }

    public String getCreditCardExpiryDate() {
        return creditCardExpiryDate;
    }

    public void setCreditCardExpiryDate(String creditCardExpiryDate) {
        this.creditCardExpiryDate = creditCardExpiryDate;
    }

    public String getCreditCardCvv() {
        return creditCardCvv;
    }

    public void setCreditCardCvv(String creditCardCvv) {
        this.creditCardCvv = creditCardCvv;
    }

    public String getCreditCardLimit() {
        return creditCardLimit;
    }

    public void setCreditCardLimit(String creditCardLimit) {
        this.creditCardLimit = creditCardLimit;
    }

    public List<CreditCard> getExtraCreditCards() {
        return extraCreditCards;
    }

    public void setExtraCreditCards(List<CreditCard> extraCreditCards) {
        this.extraCreditCards = extraCreditCards;
    }

    public void addExtraCreditCard(CreditCard card) {
        extraCreditCards.add(card);
        card.setBankAccount(this);
    }

    public void removeExtraCreditCard(CreditCard card) {
        extraCreditCards.remove(card);
        card.setBankAccount(null);
    }

    public String getNomineeName() {
        return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getNomineeRelationship() {
        return nomineeRelationship;
    }

    public void setNomineeRelationship(String nomineeRelationship) {
        this.nomineeRelationship = nomineeRelationship;
    }

    public String getEmailLinked() {
        return emailLinked;
    }

    public void setEmailLinked(String emailLinked) {
        this.emailLinked = emailLinked;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
