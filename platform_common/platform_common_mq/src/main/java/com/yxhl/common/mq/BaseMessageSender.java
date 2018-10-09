package com.yxhl.common.mq;

import java.io.Serializable;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseMessageSender<T extends Serializable> {

//	@Value("${exchange_name}")
	protected String exchangeName = "default_exchange";
	
	@Autowired
	protected RabbitTemplate rabbitTemplate;
	
	public abstract void send(T message);
}
