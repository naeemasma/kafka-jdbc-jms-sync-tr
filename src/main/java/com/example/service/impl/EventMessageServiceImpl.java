package com.example.service.impl;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.service.EventMessageDao;
import com.example.dao.service.EventMessageDetailDao;
import com.example.domain.EventMessage;
import com.example.domain.EventMessageDetail;
import com.example.jms.MessagingService;
import com.example.service.EventMessageService;
import com.example.service.event.processor.EventProcessor;

@Service("EventMessageService")
public class EventMessageServiceImpl implements EventMessageService {
	private final Logger logger = LoggerFactory.getLogger(EventMessageServiceImpl.class);
	@Autowired
    EventMessageDao eventMessageDao;
	@Autowired
    EventMessageDetailDao eventMessageDetailDao;
	@Autowired
    MessagingService messagingService;
	
	@Value("${server.port}")
	private String instanceId;
	
	@Value("${app.instance-id-error}")
	private String errorInstanceId;
	
	@Override
	public int insert(EventMessage eventMessage) {
		int rowsUpdated = eventMessageDao.insert(eventMessage);	
		EventMessageDetail msgDtl = new EventMessageDetail(eventMessage.getId(), 
        		(eventMessage.getDescription().toUpperCase().startsWith("ERROR") 
        				&& instanceId.equalsIgnoreCase(errorInstanceId))?"CRITICAL":"MEDIUM");
		eventMessageDetailDao.insert(msgDtl);
	    messagingService.sendMessage(eventMessage);	
		return rowsUpdated;
	}

}
