package com.projectManagement.demo.Services;

import com.projectManagement.demo.KafkaMessages.ActiveAccountKafkaMailMessage;
import com.projectManagement.demo.KafkaMessages.KafkaMailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import org.springframework.kafka.support.SendResult;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Value("${kafka.topics.mail}")
    private String mailTopic;

    public void sendMail(KafkaMailMessage message) {
        if(message instanceof ActiveAccountKafkaMailMessage activeAccountKafkaMailMessage){

            ObjectMapper objectMapper = new ObjectMapper();

            String messageString = objectMapper.writeValueAsString(activeAccountKafkaMailMessage);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(mailTopic,activeAccountKafkaMailMessage.getUsername(), messageString);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Message sent successfully: " + result);
                } else {
                    System.out.println("Failed to send message: " + ex.getMessage());
                }
            });
        }
    }
}



