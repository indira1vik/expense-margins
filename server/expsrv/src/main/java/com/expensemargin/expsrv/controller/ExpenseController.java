package com.expensemargin.expsrv.controller;

import com.expensemargin.expsrv.dto.ExpenseCreateRequest;
import com.expensemargin.expsrv.dto.ExpenseCreateResponse;
import com.expensemargin.expsrv.entity.Expense;
import com.expensemargin.expsrv.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ExpenseCreateResponse createExpense(@RequestBody ExpenseCreateRequest request) {
        return expenseService.createExpense(request);
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{id}")
    public Expense getExpense(@PathVariable Long id) {
        return expenseService.getExpenseById(id);
    }
}
