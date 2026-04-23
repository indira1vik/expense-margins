package com.expensemargin.notifsrv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ExpenseEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseEventConsumer.class);

    @KafkaListener(topics = "expense-created-topic", groupId = "notification-group")
    public void consume(ExpenseCreatedEvent event) {
        try {
            if (event == null) {
                logger.warn("Received null event, skipping");
                return;
            }
            
            logger.info("EVENT RECEIVED: Expense ID: {}", event.getExpenseId());
            logger.info("Expense: {}", event.getTitle());
            logger.info("Amount: {}", event.getTotalAmount());
            logger.info("Split Type: {}", event.getSplitType());
            logger.info("Created By: {}", event.getCreatedByUserId());

            logger.info("Event processed successfully for expense: {}", event.getExpenseId());
            
        } catch (Exception e) {
            logger.error("Failed to process expense created event", e);
        }
    }
}