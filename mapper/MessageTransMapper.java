package com.record.mapper;

import com.record.dto.MessageTransDto;
import com.record.model.MessageTrans;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MessageTransMapper {

    public MessageTrans mapToDomain(MessageTransDto messageTransDto) {
        MessageTrans messageTrans = new MessageTrans();
        messageTrans.setId(messageTransDto.getId());
        messageTrans.setMessage(messageTransDto.getMessage());
        messageTrans.setMessageDatetime(new Date());
        messageTrans.setOriginMessageId(messageTransDto.getOriginMessageId());
        messageTrans.setLayerId(messageTransDto.getLayerId());
        messageTrans.setRiskId(messageTransDto.getRiskId());
        messageTrans.setSenderEntityId(messageTransDto.getSenderEntityId());
        messageTrans.setSenderName(messageTransDto.getSenderUser());
        return messageTrans;
    }

    public MessageTransDto mapToModel(MessageTrans messageTrans) {
        MessageTransDto messageTransDto = new MessageTransDto();
        messageTransDto.setMessage(messageTrans.getMessage());
        messageTransDto.setOriginMessageId(messageTrans.getOriginMessageId());
        messageTransDto.setLayerId(messageTrans.getLayerId());
        messageTransDto.setRiskId(messageTrans.getRiskId());
        messageTransDto.setSenderEntityId(messageTrans.getSenderEntityId());
        messageTransDto.setSenderUser(messageTrans.getSenderName());
        return messageTransDto;
    }
}
