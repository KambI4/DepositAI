package com.coursework.deposit.controller;

import com.coursework.deposit.model.DepositEntity;
import com.coursework.deposit.model.DepositType;
import com.coursework.deposit.repository.DepositRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/deposits")
public class AdminDepositController {

    private final DepositRepository depositRepository;

    public AdminDepositController(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    // --- LIST PAGE ---
    @GetMapping
    public String listDeposits(Model model) {
        model.addAttribute("deposits", depositRepository.findAll());
        return "admin-deposits"; // HTML template
    }

    // --- ADD FORM PAGE ---
    @GetMapping("/add")
    public String addDepositForm(Model model) {
        model.addAttribute("deposit", new DepositEntity());
        model.addAttribute("types", DepositType.values());
        return "admin-deposit-form";
    }

    // --- SAVE NEW ---
    @PostMapping("/add")
    public String saveDeposit(@ModelAttribute DepositEntity deposit) {
        depositRepository.save(deposit);
        return "redirect:/admin/deposits";
    }

    // --- EDIT FORM ---
    @GetMapping("/edit/{id}")
    public String editDeposit(@PathVariable Long id, Model model) {
        DepositEntity deposit = depositRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Депозит не найден"));

        model.addAttribute("deposit", deposit);
        model.addAttribute("types", DepositType.values());

        return "admin-deposit-form";
    }

    // --- UPDATE ---
    @PostMapping("/edit/{id}")
    public String updateDeposit(
            @PathVariable Long id,
            @ModelAttribute DepositEntity updated
    ) {
        updated.setId(id);
        depositRepository.save(updated);
        return "redirect:/admin/deposits";
    }

    // --- DELETE ---
    @GetMapping("/delete/{id}")
    public String deleteDeposit(@PathVariable Long id) {
        depositRepository.deleteById(id);
        return "redirect:/admin/deposits";
    }
}
