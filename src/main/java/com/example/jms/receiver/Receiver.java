package com.example.jms.receiver;

import javax.jms.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import com.example.domain.EventMessage;

@Component
public class Receiver {

  private final Logger logger = LoggerFactory.getLogger(Receiver.class);
	
  @Bean 
  public JmsListenerContainerFactory<?> myFactory(ConnectionFactory
		  connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
	  DefaultJmsListenerContainerFactory factory = new
			  DefaultJmsListenerContainerFactory(); 
	  // This provides all boot's default to this factory, including the message converter 
	  configurer.configure(factory, connectionFactory); 
	  // You could still override some of Boot's default if necessary. 
  	return factory; 
  }
 
  @JmsListener(destination = "messagedestination", containerFactory = "myFactory")
  public void receiveMessage(EventMessage eventMessage) {
	logger.info("JMS Received:" + eventMessage);
  }
}