package com.projectManagement.demo.KafkaMessages;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class KafkaMailMessage {

    String toEmail;
    String subject;


}
