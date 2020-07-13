package com.example.jms.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.example.domain.EventMessage;

@Component
public class Receiver {

  private final Logger logger = LoggerFactory.getLogger(Receiver.class);
	
  @JmsListener(destination = "messagedestination", containerFactory = "myFactory")
  public void receiveMessage(EventMessage eventMessage) {
	logger.info("JMS Received:" + eventMessage);
  }
}