package com.yxhl.common.mq.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yxhl.common.mq.MailMessageSender;
import com.yxhl.platform.common.message.MailDto;

/**
 * @author liwei
 * @description 邮件发送类
 * @date 2017/7/19
 */
@Component
public class MailSendService {

    private static final Logger LOG = LoggerFactory.getLogger(MailSendService.class);

    @Autowired
    private MailMessageSender messageSender;

    /**
     * 发送简单文本及内嵌Html邮件
     * @param messageDto
     */
    public void sendMsg(MailDto messageDto) {
        /**
         * 进入rabbitmq
         */
        LOG.info("进入rabbitmq" + messageDto.toString());
        try {
            messageSender.send(messageDto);
        } catch (Exception ex) {
        	LOG.error("mail send error:", ex);
        }
    }
}
