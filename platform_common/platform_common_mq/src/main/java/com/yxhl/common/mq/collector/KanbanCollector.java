package com.yxhl.common.mq.collector;

import com.yxhl.common.mq.KanbanMessageSender;
import com.yxhl.platform.common.message.KanbanMessageDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @class_name KanbanCollector
 * @description 看板信息同步类
 */
@Component
public class KanbanCollector {

    private static final Logger LOG = LoggerFactory.getLogger(KanbanCollector.class);

    @Autowired
    private KanbanMessageSender messageSender;

    public void sendMsg(KanbanMessageDto messageDto) {
        /**
         * 进入rabbitmq
         */
        LOG.info("进入rabbitmq" + messageDto.toString());
        try {
            messageSender.send(messageDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
