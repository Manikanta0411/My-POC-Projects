package com.record.repository;

import com.record.model.AttachmentTrans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentTransRepository extends JpaRepository<AttachmentTrans, Long> {
    List<AttachmentTrans> findByStatusAndAttachmentIdIn(String status, List<Long> attachmentIds);
    @Query(value = "select * from ATTACHMENT_TRANS where status='UNREAD'" ,nativeQuery = true)
    List<AttachmentTrans> findAllUnreadAttachments();
    
    
}
