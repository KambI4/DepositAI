package com.coursework.deposit.controller;

import com.coursework.deposit.model.DepositEntity;
import com.coursework.deposit.model.DepositType;
import com.coursework.deposit.service.DepositService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class DepositController {

    private final DepositService service;

    public DepositController(DepositService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model,
                        Principal principal,
                        @RequestParam(value = "incomeResult", required = false) Double incomeResult,
                        @RequestParam(value = "bestBank", required = false) String bestBank) {

        List<DepositEntity> deposits = service.getAll();
        model.addAttribute("deposits", deposits);
        model.addAttribute("types", DepositType.values());
        model.addAttribute("incomeResult", incomeResult);
        model.addAttribute("bestBank", bestBank);

        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }

        return "index";
    }

    @PostMapping("/deposits/add")
    public String addDeposit(@RequestParam String bankName,
                             @RequestParam double rate,
                             @RequestParam double minAmount,
                             @RequestParam int periodMonths,
                             @RequestParam DepositType type) {
        service.add(bankName, rate, minAmount, periodMonths, type);
        return "redirect:/";
    }

    @PostMapping("/deposits/calc")
    public String calculateIncome(@RequestParam Long depositId,
                                  @RequestParam double amount) {
        double income = service.calculateIncome(depositId, amount);
        return "redirect:/?incomeResult=" + income;
    }

    @PostMapping("/deposits/best")
    public String bestDeposit(@RequestParam double amount) {
        DepositEntity best = service.findBest(amount);
        if (best == null) {
            return "redirect:/";
        }
        double income = service.calculateIncome(best.getId(), amount);
        String bank = best.getBankName();
        return "redirect:/?incomeResult=" + income + "&bestBank=" + bank;
    }
}
