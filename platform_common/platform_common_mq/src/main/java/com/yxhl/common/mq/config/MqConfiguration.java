package com.yxhl.common.mq.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * MQ队列配置
 * 
 * @author liu.tao
 *
 */
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@EnableRabbit
public class MqConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(MqConfiguration.class);

	private String exchangeName = "default_exchange";

	private String host;

	private Integer port;

	private String virtualHost;

	private String username;

	private String password;

	@Bean("elMqConnectionFactory")
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
		connectionFactory.setVirtualHost(virtualHost);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setPort(port);
		connectionFactory.setChannelCacheSize(10);
		connectionFactory.setRequestedHeartBeat(10);

		connectionFactory.setPublisherReturns(true);
		connectionFactory.setPublisherConfirms(true);
		return connectionFactory;
	}

	@Bean
	public AmqpTemplate rabbitTemplate(@Qualifier("elMqConnectionFactory") ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		RetryTemplate retryTemplate = new RetryTemplate();
		ExponentialBackOffPolicy backOffpolicy = new ExponentialBackOffPolicy();
		backOffpolicy.setInitialInterval(500);
		backOffpolicy.setMaxInterval(10000);
		backOffpolicy.setMultiplier(10.0);
		retryTemplate.setBackOffPolicy(backOffpolicy);
		rabbitTemplate.setRetryTemplate(retryTemplate);
		return rabbitTemplate;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(@Qualifier("elMqConnectionFactory") ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setConcurrentConsumers(10);
		factory.setDefaultRequeueRejected(true);

		Executor taskExecutor = new ThreadPoolExecutor(8, 20, 60000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(200),
			new ThreadPoolExecutor.CallerRunsPolicy());;
		factory.setTaskExecutor(taskExecutor);
		return factory;
	}

	@Bean
	public RabbitAdmin rabbitAdminForSierraUpdaterBibs(@Qualifier("elMqConnectionFactory") ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	@Bean
	public RabbitTemplate createRabbitTemplate(@Qualifier("elMqConnectionFactory") ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		rabbitTemplate.setMandatory(true);

		rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if (!ack) {
					LOGGER.info("================");
					LOGGER.info("correlationData = " + correlationData);
					LOGGER.info("ack = " + ack);
					LOGGER.info("cause = " + cause);
					LOGGER.info("================");
				}
			}
		});

		rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
			@Override
			public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
				LOGGER.info("================");
				LOGGER.info("message = " + message);
				LOGGER.info("replyCode = " + replyCode);
				LOGGER.info("replyText = " + replyText);
				LOGGER.info("exchange = " + exchange);
				LOGGER.info("routingKey = " + routingKey);
				LOGGER.info("================");
			}
		});

		return rabbitTemplate;
	}

	@Bean(name = "directExchange")
	DirectExchange directExchange() {
		return new DirectExchange(exchangeName, false, false);
	}

	@Bean
	public MessageConverter JSONMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
