package com.yxhl.common.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.yxhl.common.mq.config.MessageQueue.Queues;
import com.yxhl.platform.common.message.KanbanMessageDto;

/**
 * 消息发送服务，将消息发送到MQ中。
 * 
 * @author liu.tao
 *
 */
@Component
public class KanbanMessageSender extends BaseMessageSender<KanbanMessageDto> {

	private static final Logger LOGGER = LoggerFactory.getLogger(KanbanMessageSender.class);

	public static final String ROUTING_KEY = "ROUTE_KANBAN";

	@Bean(name="kanban_queue")
	Queue queue() {
	    return new Queue(Queues.kanban_queue.name(), false);
	}

	@Bean(name="kanban_binding")
	Binding binding(@Qualifier("kanban_queue") Queue queue, @Qualifier("directExchange") DirectExchange exchange) {
		System.out.println(exchange);
	    return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
	}
	public void send(KanbanMessageDto message) {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
		messageProperties.setConsumerQueue(Queues.kanban_queue.name());
		LOGGER.info("发送kanban MQ消息：" + message.toString());
		Message msg = new Message(message.toString().getBytes(), messageProperties);
		rabbitTemplate.convertAndSend(this.exchangeName, ROUTING_KEY, msg);
	}
}
