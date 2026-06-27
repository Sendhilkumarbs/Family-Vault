package com.bankmanager.controller;

import com.bankmanager.model.Deposit;
import com.bankmanager.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/deposits")
public class DepositController {

    @Autowired
    private DepositService depositService;

    // ─── Deposits List ────────────────────────────────────────
    @GetMapping
    public String listDeposits(Model model,
                               @RequestParam(required = false) String search) {
        List<Deposit> deposits;
        if (search != null && !search.trim().isEmpty()) {
            deposits = depositService.search(search);
            model.addAttribute("search", search);
        } else {
            deposits = depositService.getAllDeposits();
        }
        model.addAttribute("deposits", deposits);
        model.addAttribute("totalCount", depositService.getTotalCount());
        model.addAttribute("fdCount", depositService.getFdCount());
        model.addAttribute("rdCount", depositService.getRdCount());
        return "deposits-list";
    }

    // ─── Add Deposit (Show Form) ──────────────────────────────
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("deposit", new Deposit());
        model.addAttribute("pageTitle", "Add New FD / RD");
        return "deposit-form";
    }

    // ─── Save Deposit ─────────────────────────────────────────
    @PostMapping("/save")
    public String saveDeposit(@ModelAttribute("deposit") Deposit deposit,
                              RedirectAttributes redirectAttributes) {
        depositService.saveDeposit(deposit);
        redirectAttributes.addFlashAttribute("successMessage",
                deposit.getDepositType() + " for \"" + deposit.getPersonName() + "\" saved successfully!");
        return "redirect:/deposits";
    }

    // ─── Edit Deposit (Show Form) ─────────────────────────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        Deposit deposit = depositService.getDepositById(id);
        if (deposit == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Deposit not found.");
            return "redirect:/deposits";
        }
        model.addAttribute("deposit", deposit);
        model.addAttribute("pageTitle", "Edit " + deposit.getDepositType());
        return "deposit-form";
    }

    // ─── Update Deposit ───────────────────────────────────────
    @PostMapping("/update/{id}")
    public String updateDeposit(@PathVariable Long id,
                                @ModelAttribute("deposit") Deposit deposit,
                                RedirectAttributes redirectAttributes) {
        deposit.setId(id);
        Deposit existing = depositService.getDepositById(id);
        if (existing != null) {
            deposit.setCreatedAt(existing.getCreatedAt());
        }
        depositService.saveDeposit(deposit);
        redirectAttributes.addFlashAttribute("successMessage",
                deposit.getDepositType() + " for \"" + deposit.getPersonName() + "\" updated successfully!");
        return "redirect:/deposits";
    }

    // ─── View Deposit Details ─────────────────────────────────
    @GetMapping("/view/{id}")
    public String viewDeposit(@PathVariable Long id, Model model,
                              RedirectAttributes redirectAttributes) {
        Deposit deposit = depositService.getDepositById(id);
        if (deposit == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Deposit not found.");
            return "redirect:/deposits";
        }
        model.addAttribute("deposit", deposit);
        return "view-deposit";
    }

    // ─── Delete Deposit ───────────────────────────────────────
    @GetMapping("/delete/{id}")
    public String deleteDeposit(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        Deposit deposit = depositService.getDepositById(id);
        if (deposit != null) {
            depositService.deleteDeposit(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    deposit.getDepositType() + " for \"" + deposit.getPersonName() + "\" deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Deposit not found.");
        }
        return "redirect:/deposits";
    }
}
