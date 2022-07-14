package com.record.service;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.record.entity.BusinessEntityOp;
import com.record.model.Attachment;
import com.record.model.AttachmentTrans;
import com.record.model.Layer;
import com.record.repository.AttachmentRepository;
import com.record.repository.BusinessEntityRepository;

@Component
public class SynchupSchedulerAttachment {

	@Autowired
	private AttachmentTransService attachmentTransService;
	@Autowired
	private BusinessEntityRepository businessEntityRepository;
	
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private RestTemplate restTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(SynchupSchedulerAttachment.class);

    
	@Scheduled(fixedRate = 10000)
	public void readAttachmentFile() {

		LOGGER.debug("event = Process data for attachmentTrans Start");

		List<AttachmentTrans> attachementTrans = attachmentTransService.findUnreadAttachments();

		attachementTrans.stream().forEach(attachmentTransObj -> {

			LOGGER.debug("event = Reading data from attachmentTrans : " + attachmentTransObj.toString());

			Optional<BusinessEntityOp> entity = businessEntityRepository
					.findByEntityId(attachmentTransObj.getEntityId());

			if (entity.isPresent()) {

				StringBuilder URL = new StringBuilder();
				URL.append(entity.get().getEntityHostName()).append("attachment/")
						.append(attachmentTransObj.getAttachmentId());

				LOGGER.debug("event = Making RestTemplate Call to get attachment Data from Sender : " + URL);
				Attachment attachmentData = restTemplate.getForObject(URL.toString(), Attachment.class);
				if (attachmentData != null) {

					LOGGER.debug("event = Attachment Data Recived from Sender end using RestTemplate Call : "
							+ attachmentData.toString());
					Layer layer = new Layer();
					layer.setId(attachmentTransObj.getLayerId());
					attachmentData.setLayer(layer);
					Attachment msgSaved = attachmentRepository.save(attachmentData);
					LOGGER.debug("event = Attachment Data Recived Successfully and saved");
					StringBuilder downloadFileUrl = new StringBuilder();

					downloadFileUrl.append(entity.get().getEntityHostName()).append("downloadFile/")
							.append(attachmentTransObj.getAttachmentId());

					LOGGER.debug("event = Download attachment file : " + downloadFileUrl);

					StringBuilder filePath = new StringBuilder();
					filePath.append(attachmentData.getFilePath()).append("/").append(attachmentData.getFileName());
					LOGGER.debug("event = Download attachment file to location : " + filePath);
					try {
						Files.createDirectories(Paths.get(attachmentData.getFilePath()));
					} catch (IOException exception) {
						
						LOGGER.debug("event = Exception Occur while process file : ");

					}
					try (BufferedInputStream inputStream = new BufferedInputStream(
							new URL(downloadFileUrl.toString()).openStream());
							FileOutputStream fileOS = new FileOutputStream(filePath.toString())) {
						byte data[] = new byte[1024];
						int byteContent;
						while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
							fileOS.write(data, 0, byteContent);
						}
						if (byteContent == -1) {
							attachmentTransService.updateAttachmentTransStatus(attachmentTransObj.getId());
							LOGGER.debug("event = File is downloaded and save in the location : " + filePath);

						}

					} catch (IOException e) {
						e.printStackTrace();
						LOGGER.debug("event = Exception Occur while process file : ");

					}

				}

			}

		});

	}
}
