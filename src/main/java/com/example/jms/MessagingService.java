package com.example.jms;

import com.example.domain.EventMessage;

public interface MessagingService {

	public void sendMessage(EventMessage eventMessage);
}
