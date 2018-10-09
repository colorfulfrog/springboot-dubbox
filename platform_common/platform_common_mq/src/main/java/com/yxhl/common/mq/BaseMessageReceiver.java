package com.yxhl.common.mq;
import org.springframework.amqp.core.Message;

/**
 * @class_name BaseMessageReceiver
 * @description 接收mq消息
 */

public abstract class BaseMessageReceiver {

    public abstract void receiver(Message msg);
}