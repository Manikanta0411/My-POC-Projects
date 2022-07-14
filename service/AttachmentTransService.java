package com.record.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.model.AttachmentTrans;
import com.record.repository.AttachmentTransRepository;

@Service
public class AttachmentTransService {

    @Autowired
    private AttachmentTransRepository attachmentTransRepository;

    public void saveAttachments(List<Long> attachments, Long layerId, String entityId, String status) {
        attachments.stream().forEach(attachmentId -> {
            AttachmentTrans attachmentTrans = new AttachmentTrans();
            attachmentTrans.setAttachmentId(attachmentId);
            attachmentTrans.setLayerId(layerId);
            attachmentTrans.setEntityId(entityId);
            attachmentTrans.setStatus(status);
            attachmentTransRepository.save(attachmentTrans);
        });
    }

    public List<AttachmentTrans> findUnreadAttachments(List<Long> attachmentIds) {
        return attachmentTransRepository.findByStatusAndAttachmentIdIn("UNREAD", attachmentIds);
    }
    
    
    public List<AttachmentTrans> findUnreadAttachments() {
        return attachmentTransRepository.findAllUnreadAttachments();
    }
    
    public void updateAttachmentTransStatus(Long id)
    {
    	Optional<AttachmentTrans> attachmentTrans= attachmentTransRepository.findById(id);
    	if(attachmentTrans.isPresent())
    	{   attachmentTrans.get().setStatus("READ");
        	attachmentTransRepository.save(attachmentTrans.get());
    	}
    }
    
    
    
}
