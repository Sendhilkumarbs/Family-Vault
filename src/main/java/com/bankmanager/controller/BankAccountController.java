package com.bankmanager.controller;

import com.bankmanager.model.BankAccount;
import com.bankmanager.model.CreditCard;
import com.bankmanager.service.BankAccountService;
import com.bankmanager.service.DepositService;
import com.bankmanager.service.ExcelExportService;
import com.bankmanager.service.InsuranceService;
import com.bankmanager.service.ReminderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class BankAccountController {

    @Autowired
    private BankAccountService accountService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ExcelExportService excelExportService;

    // ─── Dashboard (Tiles) ───────────────────────────────────
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("accountCount", accountService.getTotalCount());
        model.addAttribute("fdCount", depositService.getFdCount());
        model.addAttribute("rdCount", depositService.getRdCount());
        model.addAttribute("insuranceCount", insuranceService.getTotalCount());
        model.addAttribute("reminders", reminderService.getUpcomingReminders());
        model.addAttribute("overdueCount", reminderService.getOverdueCount());
        model.addAttribute("dueSoonCount", reminderService.getDueSoonCount());
        return "dashboard";
    }

    // ─── Accounts List ───────────────────────────────────────
    @GetMapping("/accounts")
    public String listAccounts(Model model,
                               @RequestParam(required = false) String search) {
        List<BankAccount> accounts;
        if (search != null && !search.trim().isEmpty()) {
            accounts = accountService.search(search);
            model.addAttribute("search", search);
        } else {
            accounts = accountService.getAllAccounts();
        }
        model.addAttribute("accounts", accounts);
        model.addAttribute("totalCount", accountService.getTotalCount());
        return "accounts-list";
    }

    // ─── Add Account (Show Form) ─────────────────────────────
    @GetMapping("/accounts/new")
    public String showAddForm(Model model) {
        model.addAttribute("account", new BankAccount());
        model.addAttribute("pageTitle", "Add New Account");
        return "account-form";
    }

    // ─── Save Account ────────────────────────────────────────
    @PostMapping("/accounts/save")
    public String saveAccount(@ModelAttribute("account") BankAccount account,
                              RedirectAttributes redirectAttributes) {
        // Link extra credit cards back to this account
        linkCreditCards(account);
        accountService.saveAccount(account);
        redirectAttributes.addFlashAttribute("successMessage",
                "Account for \"" + account.getPersonName() + "\" saved successfully!");
        return "redirect:/accounts";
    }

    // ─── Edit Account (Show Form) ────────────────────────────
    @GetMapping("/accounts/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        BankAccount account = accountService.getAccountById(id);
        if (account == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Account not found.");
            return "redirect:/accounts";
        }
        model.addAttribute("account", account);
        model.addAttribute("pageTitle", "Edit Account");
        return "account-form";
    }

    // ─── Update Account ──────────────────────────────────────
    @PostMapping("/accounts/update/{id}")
    public String updateAccount(@PathVariable Long id,
                                @ModelAttribute("account") BankAccount account,
                                RedirectAttributes redirectAttributes) {
        account.setId(id);
        // preserve original created date
        BankAccount existing = accountService.getAccountById(id);
        if (existing != null) {
            account.setCreatedAt(existing.getCreatedAt());
        }
        // Link extra credit cards back to this account
        linkCreditCards(account);
        accountService.saveAccount(account);
        redirectAttributes.addFlashAttribute("successMessage",
                "Account for \"" + account.getPersonName() + "\" updated successfully!");
        return "redirect:/accounts";
    }

    // ─── View Account Details ────────────────────────────────
    @GetMapping("/accounts/view/{id}")
    public String viewAccount(@PathVariable Long id, Model model,
                              RedirectAttributes redirectAttributes) {
        BankAccount account = accountService.getAccountById(id);
        if (account == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Account not found.");
            return "redirect:/accounts";
        }
        model.addAttribute("account", account);
        return "view-account";
    }

    // ─── Delete Account ──────────────────────────────────────
    @GetMapping("/accounts/delete/{id}")
    public String deleteAccount(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        BankAccount account = accountService.getAccountById(id);
        if (account != null) {
            accountService.deleteAccount(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Account for \"" + account.getPersonName() + "\" deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Account not found.");
        }
        return "redirect:/accounts";
    }

    // ─── Export to Excel ─────────────────────────────────────
    @GetMapping("/accounts/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment; filename=FamilyBankDetails.xlsx");

        List<BankAccount> accounts = accountService.getAllAccounts();
        excelExportService.exportToExcel(accounts, response.getOutputStream());
        response.getOutputStream().flush();
    }

    // ─── Helper: link credit cards to parent account ─────────
    private void linkCreditCards(BankAccount account) {
        if (account.getExtraCreditCards() != null) {
            // Remove cards with all empty fields (blank rows)
            account.getExtraCreditCards().removeIf(card ->
                    isBlank(card.getCardName()) &&
                    isBlank(card.getCardNumber()) &&
                    isBlank(card.getCardPin()) &&
                    isBlank(card.getExpiryDate()) &&
                    isBlank(card.getCvv()) &&
                    isBlank(card.getCreditLimit()));

            for (CreditCard card : account.getExtraCreditCards()) {
                card.setBankAccount(account);
            }
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
