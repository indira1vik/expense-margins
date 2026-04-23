package com.expensemargin.expsrv.service;

import com.expensemargin.expsrv.dto.ExpenseCreateRequest;
import com.expensemargin.expsrv.dto.ExpenseUserSplit;
import com.expensemargin.expsrv.entity.ExpenseParticipant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SplitService {
    public SplitService() {}
    public List<ExpenseParticipant> calculateSplits(Long expenseId, ExpenseCreateRequest request) {
        List<ExpenseParticipant> result = new ArrayList<>();
        switch (request.getSplitType()) {
            case "EQUAL":
                double equalShare = request.getTotalAmount() / request.getSplits().size();
                for (ExpenseUserSplit split : request.getSplits()) {
                    ExpenseParticipant ep = new ExpenseParticipant();
                    ep.setExpenseId(expenseId);
                    ep.setUserId(split.getUserId());
                    ep.setAmountOwed(equalShare);
                    ep.setStatus("OWED");
                    result.add(ep);
                }
                break;

            case "EXACT":
                for (ExpenseUserSplit split : request.getSplits()) {
                    ExpenseParticipant ep = new ExpenseParticipant();
                    ep.setExpenseId(expenseId);
                    ep.setUserId(split.getUserId());
                    ep.setAmountOwed(split.getAmount());
                    ep.setStatus("OWED");
                    result.add(ep);
                }
                break;

            case "PERCENT":
                for (ExpenseUserSplit split : request.getSplits()) {
                    double amount = (request.getTotalAmount() * split.getPercentage()) / 100;
                    ExpenseParticipant ep = new ExpenseParticipant();
                    ep.setExpenseId(expenseId);
                    ep.setUserId(split.getUserId());
                    ep.setAmountOwed(amount);
                    ep.setStatus("OWED");
                    result.add(ep);
                }
                break;
        }
        return result;
    }
}
