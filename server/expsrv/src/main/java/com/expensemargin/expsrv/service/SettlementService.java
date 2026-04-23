package com.expensemargin.expsrv.service;

import com.expensemargin.expsrv.entity.Expense;
import com.expensemargin.expsrv.entity.ExpenseParticipant;
import com.expensemargin.expsrv.entity.Settlement;
import com.expensemargin.expsrv.repository.SettlementRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Service
public class SettlementService {
    private static final double EPSILON = 0.000001;
    private static final String STATUS_PENDING = "PENDING";

    private final SettlementRepository settlementRepository;

    public SettlementService(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    public class UserBalance {
        String userId;
        double amount;

        public UserBalance(String userId, double amount) {
            this.userId = userId;
            this.amount = amount;
        }
    }

    public static class SettlementInstruction {
        private final String fromUserId;
        private final String toUserId;
        private final double amount;

        public SettlementInstruction(String fromUserId, String toUserId, double amount) {
            this.fromUserId = fromUserId;
            this.toUserId = toUserId;
            this.amount = amount;
        }

        public String getFromUserId() {
            return fromUserId;
        }

        public String getToUserId() {
            return toUserId;
        }

        public double getAmount() {
            return amount;
        }
    }

    public List<SettlementInstruction> settle(Expense expense, List<ExpenseParticipant> participants) {
        Map<String, Double> balanceMap = new HashMap<>();
        List<SettlementInstruction> settlements = new ArrayList<>();

        for (ExpenseParticipant p : participants) {
            // payer gets positive balance
            if (p.getUserId().equals(expense.getCreatedByUserId())) {
                balanceMap.put(p.getUserId(),
                        balanceMap.getOrDefault(p.getUserId(), 0.0) + (expense.getTotalAmount() - p.getAmountOwed()));
            } else {
                balanceMap.put(p.getUserId(),
                        balanceMap.getOrDefault(p.getUserId(), 0.0) - p.getAmountOwed());
            }
        }
        // STEP 2: build heaps
        PriorityQueue<UserBalance> creditors = new PriorityQueue<>(
                (a, b) -> Double.compare(b.amount, a.amount)
        );

        PriorityQueue<UserBalance> debtors = new PriorityQueue<>(
                (a, b) -> Double.compare(a.amount, b.amount)
        );
        for (Map.Entry<String, Double> entry : balanceMap.entrySet()) {
            if (entry.getValue() > 0) {
                creditors.add(new UserBalance(entry.getKey(), entry.getValue()));
            } else if (entry.getValue() < 0) {
                debtors.add(new UserBalance(entry.getKey(), entry.getValue()));
            }
        }

        // STEP 3: settlement
        while (!creditors.isEmpty() && !debtors.isEmpty()) {

            UserBalance creditor = creditors.poll();
            UserBalance debtor = debtors.poll();

            double settledAmount = Math.min(creditor.amount, -debtor.amount);
            if (settledAmount <= EPSILON) {
                continue;
            }

            settlements.add(new SettlementInstruction(debtor.userId, creditor.userId, settledAmount));

            creditor.amount -= settledAmount;
            debtor.amount += settledAmount;

            if (creditor.amount > EPSILON) creditors.add(creditor);
            if (debtor.amount < -EPSILON) debtors.add(debtor);
        }

        return settlements;
    }

    public List<Settlement> saveSettlements(Long expenseId, List<SettlementInstruction> instructions) {
        List<Settlement> rows = new ArrayList<>();
        Date now = new Date();

        for (SettlementInstruction instruction : instructions) {
            Settlement settlement = new Settlement();
            settlement.setExpenseId(expenseId);
            settlement.setFromUserId(instruction.getFromUserId());
            settlement.setToUserId(instruction.getToUserId());
            settlement.setAmount(instruction.getAmount());
            settlement.setStatus(STATUS_PENDING);
            settlement.setCreatedAt(now);
            rows.add(settlement);
        }

        return settlementRepository.saveAll(rows);
    }

}
