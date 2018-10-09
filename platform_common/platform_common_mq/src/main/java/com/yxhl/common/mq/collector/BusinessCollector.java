package com.yxhl.common.mq.collector;

import com.yxhl.common.mq.BusinessMessageSender;
import com.yxhl.platform.common.message.BusinessMessageDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @class_name BusinessCollector
 * @description 需求服务同步类
 */
@Component
public class BusinessCollector {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessCollector.class);

    @Autowired
    private BusinessMessageSender messageSender;
    public  void sendMsg(BusinessMessageDto messageDto) {
        /**
         * 进入rabbitmq
         */
        LOG.info(messageDto.toString());
        try {
            messageSender.send(messageDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}