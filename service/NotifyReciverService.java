package com.record.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.entity.NotifyEntity;
import com.record.model.NotifyModel;
import com.record.repository.NotifyReceiverRepository;
import com.record.util.MapperUtil;

@Service
public class NotifyReciverService {

	@Autowired
	public NotifyReceiverRepository notifyReceiverRepository;

	public NotifyModel save(NotifyModel req) {
		NotifyEntity notifyEntity = MapperUtil.convertModelToEntityStrict(req, NotifyEntity.class);
		notifyEntity.setIsSentByMe(false);
		return MapperUtil.convertEntityToModel(notifyReceiverRepository.save(notifyEntity), NotifyModel.class);
	}

	public List<NotifyModel> getUnReadMessage() {
		return notifyReceiverRepository.findAll().stream()
				.map(notifyEntity -> MapperUtil.convertEntityToModel(notifyEntity, NotifyModel.class))
				.collect(Collectors.toList());
	}

	public List<NotifyModel> getAllUnReadMessage() {
		return notifyReceiverRepository.findUnReadNotificationSendByOtherEntity().stream()
				.map(notifyEntity -> MapperUtil.convertEntityToModel(notifyEntity, NotifyModel.class))
				.collect(Collectors.toList());
	}
          
	public void updateInboxStatus(String id, String status) {
		NotifyEntity notifyEntity = notifyReceiverRepository.findByOriginMessageId(id).get();
		if ((status.equalsIgnoreCase("true"))) {
			notifyEntity.setIsRead(true);
		}
		if ((status.equalsIgnoreCase("false"))) {
			notifyEntity.setIsRead(false);
		}

		notifyReceiverRepository.save(notifyEntity);
	}

}
