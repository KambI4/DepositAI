package com.coursework.deposit.controller;

import com.coursework.deposit.model.DepositEntity;
import com.coursework.deposit.model.DepositType;
import com.coursework.deposit.model.UserEntity;
import com.coursework.deposit.repository.DepositRepository;
import com.coursework.deposit.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
public class DepositController {

    private final DepositRepository depositRepository;
    private final UserRepository userRepository;

    public DepositController(DepositRepository depositRepository,
                             UserRepository userRepository) {
        this.depositRepository = depositRepository;
        this.userRepository = userRepository;
    }

    // ---------- ГЛАВНАЯ СТРАНИЦА ----------
    @GetMapping("/")
    public String index(Model model, Principal principal) {

        List<DepositEntity> deposits = depositRepository.findAll();

        model.addAttribute("deposits", deposits);
        model.addAttribute("types", DepositType.values());

        model.addAttribute("username", principal != null ? principal.getName() : null);

        return "index";
    }

    // ---------- СОЗДАНИЕ ДЕПОЗИТА ----------
    @PostMapping("/deposits/add")
    public String addDeposit(@RequestParam String bankName,
                             @RequestParam("rate") double interestRate,
                             @RequestParam double minAmount,
                             @RequestParam int periodMonths,
                             @RequestParam DepositType type,
                             Principal principal) {

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        UserEntity owner = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        DepositEntity deposit = new DepositEntity();
        deposit.setBankName(bankName);
        deposit.setInterestRate(interestRate);
        deposit.setMinAmount(minAmount);
        deposit.setPeriodMonths(periodMonths);
        deposit.setType(type);
        deposit.setOwner(owner);

        depositRepository.save(deposit);
        return "redirect:/";
    }

    // ---------- ФОРМА РЕДАКТИРОВАНИЯ ----------
    @GetMapping("/deposits/edit/{id}")
    public String editDepositForm(@PathVariable Long id,
                                  Model model,
                                  Principal principal) {

        DepositEntity deposit = depositRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        checkOwnerOrThrow(deposit, principal);

        model.addAttribute("deposit", deposit);
        model.addAttribute("types", DepositType.values());
        model.addAttribute("username", principal.getName());

        return "edit-deposit";
    }

    // ---------- ОБНОВЛЕНИЕ ----------
    @PostMapping("/deposits/edit/{id}")
    public String updateDeposit(@PathVariable Long id,
                                @RequestParam String bankName,
                                @RequestParam("rate") double interestRate,
                                @RequestParam double minAmount,
                                @RequestParam int periodMonths,
                                @RequestParam DepositType type,
                                Principal principal) {

        DepositEntity deposit = depositRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        checkOwnerOrThrow(deposit, principal);

        deposit.setBankName(bankName);
        deposit.setInterestRate(interestRate);
        deposit.setMinAmount(minAmount);
        deposit.setPeriodMonths(periodMonths);
        deposit.setType(type);

        depositRepository.save(deposit);
        return "redirect:/";
    }

    // ---------- УДАЛЕНИЕ ----------
    @GetMapping("/deposits/delete/{id}")
    public String deleteDeposit(@PathVariable Long id,
                                Principal principal) {

        DepositEntity deposit = depositRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        checkOwnerOrThrow(deposit, principal);

        depositRepository.delete(deposit);
        return "redirect:/";
    }

    // ---------- Проверка владельца ----------
    private void checkOwnerOrThrow(DepositEntity deposit, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if (deposit.getOwner() == null || !deposit.getOwner().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
