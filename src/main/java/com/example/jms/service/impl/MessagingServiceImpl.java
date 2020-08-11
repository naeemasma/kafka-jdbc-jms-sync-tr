package com.example.jms.service.impl;

import javax.jms.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Service;
import com.example.domain.EventMessage;
import com.example.jms.MessagingService;

@Service("MessagingService")
public class MessagingServiceImpl implements MessagingService{
	
	private static final Logger logger = LoggerFactory.getLogger(MessagingServiceImpl.class);

	
	  @Autowired 
	  private ApplicationContext appContext;
	  
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
	 

	  @Bean // Serialize message content to json using TextMessage
	  public MessageConverter jacksonJmsMessageConverter() {
	    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
	    converter.setTargetType(MessageType.TEXT);
	    converter.setTypeIdPropertyName("_type");
	    return converter;
	  }

	@Override
	public void sendMessage(EventMessage eventMessage) {
		JmsTemplate jmsTemplate = appContext.getBean(JmsTemplate.class);
		if(eventMessage.getDescription().toUpperCase().startsWith("JMSERR"))
			throw new RuntimeException("Message Error: JMSERR");
	    // Send a message with a POJO - the template reuse the message converter
	    logger.info("JMS Sending message: " + eventMessage);
	    jmsTemplate.convertAndSend("messagedestination", eventMessage);
	}
}
