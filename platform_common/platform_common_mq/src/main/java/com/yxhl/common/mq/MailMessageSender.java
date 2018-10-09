package com.yxhl.common.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.yxhl.common.mq.config.MessageQueue.Queues;
import com.yxhl.platform.common.message.MailDto;

/**
 * 消息发送服务，将消息发送到MQ中。
 * 
 * @author liu.tao
 *
 */
@Component
public class MailMessageSender extends BaseMessageSender<MailDto> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailMessageSender.class);

	public static final String ROUTING_KEY = "ROUTE_MAIL";

	@Bean(name="mail_queue")
	Queue queue() {
	    return new Queue(Queues.mail_queue.name(), false);
	}

	@Bean(name="mail_binding")
	Binding binding(@Qualifier("mail_queue") Queue queue, @Qualifier("directExchange") DirectExchange exchange) {
	    return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
	}
	public void send(MailDto mail) {
		LOGGER.info("发送mail MQ消息：" + mail.toString());
		rabbitTemplate.convertAndSend(this.exchangeName, ROUTING_KEY, mail);
	}
}
