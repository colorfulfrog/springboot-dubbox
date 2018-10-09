package com.yxhl.common.mq.config;

/**
 * 队列类型
 * 
 * @author liu.tao
 *
 */
public class MessageQueue {
	public enum Queues {
		/**
		 * 动态队列
		 */
		activity_queue,
		/**
		 * 看板队列
		 */
		kanban_queue,
		/**
		 * 邮件队列
		 */
		mail_queue,
		/**
		 * 业务队列
		 */
		business_queue
	}
}
