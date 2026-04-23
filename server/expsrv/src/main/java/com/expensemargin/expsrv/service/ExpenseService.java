package com.expensemargin.expsrv.service;

import com.expensemargin.expsrv.dto.ExpenseCreateRequest;
import com.expensemargin.expsrv.dto.ExpenseCreateResponse;
import com.expensemargin.expsrv.entity.Expense;
import com.expensemargin.expsrv.entity.ExpenseCreatedEvent;
import com.expensemargin.expsrv.entity.ExpenseParticipant;
import com.expensemargin.expsrv.entity.Settlement;
import com.expensemargin.expsrv.repository.ExpenseParticipantRepo;
import com.expensemargin.expsrv.repository.ExpenseRepository;
import com.expensemargin.expsrv.dto.ExpenseUserSplit;
import com.expensemargin.expsrv.dto.SettlementResponse;
import com.expensemargin.grpc.UserRequest;
import com.expensemargin.grpc.UserResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseParticipantRepo expenseParticipantRepo;
    private final SplitService splitService;
    private final SettlementService settlementService;
    private final ExpenseEventProducer expenseEventProducer;

    @GrpcClient("user-service")
    private com.expensemargin.grpc.UserServiceGrpc.UserServiceBlockingStub userStub;

    private void validateUser(String userId) {
        validateCanonicalUserId(userId);

        UserRequest request = UserRequest.newBuilder()
                .setUserId(userId)
                .build();
        UserResponse response = userStub.getUser(request);
        if (response == null || response.getUserId().isEmpty()) {
            throw new RuntimeException("Invalid user");
        }
    }

    private void validateCanonicalUserId(String userId) {
        try {
            UUID.fromString(userId);
        } catch (Exception ex) {
            throw new RuntimeException("User ID must be a valid UUID");
        }
    }

    private void validateAllUsers(ExpenseCreateRequest request) {
        Set<String> userIds = new HashSet<>();
        userIds.add(request.getCreatedByUserId());

        if (request.getSplits() != null) {
            for (ExpenseUserSplit split : request.getSplits()) {
                if (split.getUserId() != null && !split.getUserId().isBlank()) {
                    userIds.add(split.getUserId());
                }
            }
        }

        for (String userId : userIds) {
            validateUser(userId);
        }
    }


    public ExpenseService(ExpenseRepository expenseRepository, ExpenseParticipantRepo expenseParticipantRepo,
                          SplitService splitService, SettlementService settlementService,
                          ExpenseEventProducer expenseEventProducer) {
        this.expenseRepository = expenseRepository;
        this.expenseParticipantRepo = expenseParticipantRepo;
        this.splitService = splitService;
        this.settlementService = settlementService;
        this.expenseEventProducer = expenseEventProducer;
    }

    @Transactional
    public ExpenseCreateResponse createExpense(ExpenseCreateRequest request) {
        switch (request.getSplitType()) {
            case "EXACT":
                validateExact(request);
                break;
            case "PERCENT":
                validatePercent(request);
                break;
            case "EQUAL":
                break;
        }
        validateAllUsers(request);
        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setTotalAmount(request.getTotalAmount());
        expense.setCurrency(request.getCurrency());
        expense.setCreatedByUserId(request.getCreatedByUserId());
        expense.setCreatedAt(new java.util.Date());
        Expense savedExpense = expenseRepository.save(expense);
        ExpenseCreatedEvent event = new ExpenseCreatedEvent(
                savedExpense.getId(),
                savedExpense.getTitle(),
                savedExpense.getTotalAmount(),
                savedExpense.getCreatedByUserId(),
                request.getSplitType()
        );
        expenseEventProducer.publishExpenseCreated(event);
        List<ExpenseParticipant> participants = splitService.calculateSplits(savedExpense.getId(), request);
        expenseParticipantRepo.saveAll(participants);
        List<SettlementService.SettlementInstruction> settlements = settlementService.settle(savedExpense, participants);
        List<Settlement> savedSettlements = settlementService.saveSettlements(savedExpense.getId(), settlements);

        List<SettlementResponse> settlementResponse = savedSettlements.stream()
            .map(s -> new SettlementResponse(
                s.getId(),
                s.getExpenseId(),
                s.getFromUserId(),
                s.getToUserId(),
                s.getAmount(),
                s.getStatus()
            ))
            .collect(Collectors.toList());

        return new ExpenseCreateResponse(savedExpense, settlementResponse);
    }

    private void validatePercent(ExpenseCreateRequest request) {
        double sum = request.getSplits()
                .stream()
                .mapToDouble(ExpenseUserSplit::getPercentage)
                .sum();
        if (Math.abs(sum - 100.0) > 0.01) {
            throw new RuntimeException("PERCENT split must be equal to 100%");
        }
    }

    private void validateExact(ExpenseCreateRequest request) {
        double sum = request.getSplits()
                .stream()
                .mapToDouble(ExpenseUserSplit::getAmount)
                .sum();
        if (Math.abs(sum - request.getTotalAmount()) > 0.01) {
            throw new RuntimeException("EXACT split does not match total amount");
        }
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }
}


