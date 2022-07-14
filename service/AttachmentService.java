package com.record.service;

import static com.record.constant.LastOpIndicator.I;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.record.entity.AttachmentEntityLink;
import com.record.entity.BusinessEntityOp;
import com.record.exception.BadRequestException;
import com.record.model.Attachment;
import com.record.model.Layer;
import com.record.repository.AttachmentEntityLinkReposotory;
import com.record.repository.AttachmentRepository;
import com.record.repository.BusinessEntityRepository;
import com.record.repository.LayerRepository;

@Service
public class AttachmentService {

	@Autowired
	private AttachmentRepository attachmentRepository;

	@Autowired
	private LayerRepository layerRepository;

	@Autowired
	private BusinessEntityRepository businessEntityRepository;

	@Autowired
	private AttachmentEntityLinkReposotory attachmentEntityLinkReposotory;

	@Value("${file.base.path}")
	private String fileBasePath;

	public void addAttachment(Attachment attachment, Long layerId) throws IOException {
		Optional<Layer> layerOption = layerRepository.findById(layerId);
		/*
		 * Optional<Attachment> attachmentFromDB =
		 * attachmentRepository.findByFileNameAndLayerId(attachment.getFileName(),
		 * layerId); if(attachmentFromDB.isPresent()) { attachment }
		 */

		layerOption.orElseThrow(() -> new IllegalStateException("Error! Layer id to add attachment not found"));
		Layer layer = layerOption.get();

		attachment.setLayer(layer);
		attachment.setFilePath(getFilePath(layer));
		attachment.setUpdatedDate(new Date());
		attachment.setCreatedDate(new Date());
		attachment.setLastOpIndicator(I);
		attachment.setSent(false);
		saveFileToPath(attachment, layer);

		if (CollectionUtils.isEmpty(layer.getAttachments())) {
			layer.setAttachments(Arrays.asList(attachment));
		} else {
			Optional<Attachment> attachmentByName = getAttachmentByName(attachment, layer);
			if (attachmentByName.isPresent()) {
				attachmentByName.get().setSent(false);
			} else {
				layer.getAttachments().add(attachment);
			}
		}
		attachmentRepository.save(attachment);
	}

	private void saveFileToPath(Attachment attachment, Layer layer) throws IOException {
		String filePath = getFilePath(layer);
		Path path = Paths.get(filePath + File.separator);
		Files.createDirectories(path);
		OutputStream os = new FileOutputStream(new File(path.toString() + File.separator + attachment.getFileName()));
		os.write(attachment.getBytes());
		IOUtils.closeQuietly(os);
	}

	private Optional<Attachment> getAttachmentByName(Attachment attachment, Layer layer) {
		List<Attachment> attachments = layer.getAttachments();
		if (!CollectionUtils.isEmpty(attachments)) {
			return attachments.stream().filter(file -> file.getFileName().equalsIgnoreCase(attachment.getFileName()))
					.findFirst();

		}
		return null;
	}

	private String getFilePath(Layer layer) {
		String filePath = fileBasePath + File.separator + layer.getRisk().getId() + File.separator + layer.getId();
		return filePath;
	}

	public void assignEntitiesToAttachment(Long attachmentId, List<Long> entityIds) {
		Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
		if (attachment.isPresent()) {

			AttachmentEntityLink attachmentEntityLink = new AttachmentEntityLink();
			attachmentEntityLink.setAttachment(attachment.get());

			List<BusinessEntityOp> entities = businessEntityRepository.findByIdIn(new HashSet<>(entityIds));
			StringBuilder commaSepEntityString = new StringBuilder("");
			if (entities.size() > 0) {

				entities.forEach(entity -> {
					commaSepEntityString.append(entity.getId()).append(",");
				});
			} else {
				throw new EntityNotFoundException("Entity not exists!");
			}

			attachmentEntityLink
					.setEntityIds(commaSepEntityString.substring(0, commaSepEntityString.length() - 1).toString());
			attachmentEntityLinkReposotory.save(attachmentEntityLink);

		} else {
			throw new EntityNotFoundException("Attachment not exists!");
		}

	}

	public void deleteAttachment(Long attachmentId) throws BadRequestException {
		Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
		if (attachment.isPresent()) {
			attachmentRepository.deleteById(attachmentId);
		} else {
			throw new BadRequestException("Attachment not found with attachment Id " + attachmentId);
		}
	}
	
	public Attachment getAttachementById(Long attachmentId)
    {
		Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
		if(attachment.isPresent())
    	{return attachment.get();
    	}else
    	{
    		throw new EntityNotFoundException("Attachment with Id "+attachmentId+"  Not found!");
    	}
		
    	}
    }
