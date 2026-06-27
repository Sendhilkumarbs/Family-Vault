package com.bankmanager.controller;

import com.bankmanager.model.Insurance;
import com.bankmanager.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/insurance")
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    // ─── Insurance List ────────────────────────────────────────
    @GetMapping
    public String listInsurance(Model model,
                                @RequestParam(required = false) String search) {
        List<Insurance> insurances;
        if (search != null && !search.trim().isEmpty()) {
            insurances = insuranceService.search(search);
            model.addAttribute("search", search);
        } else {
            insurances = insuranceService.getAllInsurance();
        }
        model.addAttribute("insurances", insurances);
        model.addAttribute("totalCount", insuranceService.getTotalCount());
        return "insurance-list";
    }

    // ─── Add Insurance (Show Form) ──────────────────────────────
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("insurance", new Insurance());
        model.addAttribute("pageTitle", "Add New Insurance");
        return "insurance-form";
    }

    // ─── Save Insurance ─────────────────────────────────────────
    @PostMapping("/save")
    public String saveInsurance(@ModelAttribute("insurance") Insurance insurance,
                                RedirectAttributes redirectAttributes) {
        insuranceService.saveInsurance(insurance);
        redirectAttributes.addFlashAttribute("successMessage",
                insurance.getInsuranceType() + " Insurance for \"" + insurance.getPersonName() + "\" saved successfully!");
        return "redirect:/insurance";
    }

    // ─── Edit Insurance (Show Form) ─────────────────────────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        Insurance insurance = insuranceService.getInsuranceById(id);
        if (insurance == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Insurance not found.");
            return "redirect:/insurance";
        }
        model.addAttribute("insurance", insurance);
        model.addAttribute("pageTitle", "Edit Insurance");
        return "insurance-form";
    }

    // ─── Update Insurance ───────────────────────────────────────
    @PostMapping("/update/{id}")
    public String updateInsurance(@PathVariable Long id,
                                  @ModelAttribute("insurance") Insurance insurance,
                                  RedirectAttributes redirectAttributes) {
        insurance.setId(id);
        Insurance existing = insuranceService.getInsuranceById(id);
        if (existing != null) {
            insurance.setCreatedAt(existing.getCreatedAt());
        }
        insuranceService.saveInsurance(insurance);
        redirectAttributes.addFlashAttribute("successMessage",
                insurance.getInsuranceType() + " Insurance for \"" + insurance.getPersonName() + "\" updated successfully!");
        return "redirect:/insurance";
    }

    // ─── View Insurance Details ─────────────────────────────────
    @GetMapping("/view/{id}")
    public String viewInsurance(@PathVariable Long id, Model model,
                                RedirectAttributes redirectAttributes) {
        Insurance insurance = insuranceService.getInsuranceById(id);
        if (insurance == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Insurance not found.");
            return "redirect:/insurance";
        }
        model.addAttribute("insurance", insurance);
        return "view-insurance";
    }

    // ─── Delete Insurance ───────────────────────────────────────
    @GetMapping("/delete/{id}")
    public String deleteInsurance(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        Insurance insurance = insuranceService.getInsuranceById(id);
        if (insurance != null) {
            insuranceService.deleteInsurance(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    insurance.getInsuranceType() + " Insurance for \"" + insurance.getPersonName() + "\" deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Insurance not found.");
        }
        return "redirect:/insurance";
    }
}
