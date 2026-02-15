package com.projectManagement.demo.KafkaMessages;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ActiveAccountKafkaMailMessage extends KafkaMailMessage {

    String username;
    String activationLink;
    int minutes;
    int empId;


}
