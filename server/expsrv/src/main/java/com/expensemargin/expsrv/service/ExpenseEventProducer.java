package com.expensemargin.expsrv.service;

import com.expensemargin.expsrv.entity.ExpenseCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExpenseEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseEventProducer.class);
    private static final String TOPIC = "expense-created-topic";
    
    private final KafkaTemplate<String, ExpenseCreatedEvent> kafkaTemplate;
    
    public ExpenseEventProducer(KafkaTemplate<String, ExpenseCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void publishExpenseCreated(ExpenseCreatedEvent event) {
        try {
            logger.info("Publishing expense created event for expenseId: {}", event.getExpenseId());
            kafkaTemplate.send(TOPIC, event.getExpenseId().toString(), event);
            logger.info("Event published successfully");
        } catch (Exception e) {
            logger.error("Failed to publish expense created event", e);
            throw new RuntimeException("Failed to publish expense event", e);
        }
    }
}
