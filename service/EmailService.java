package com.record.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.record.entity.BusinessEntityOp;
import com.record.entity.EmailEntity;
import com.record.entity.LobEntity;
import com.record.exception.BadRequestException;
import com.record.exception.ResourceNotFoundException;
import com.record.model.EmailModel;
import com.record.model.LastOpIndicator;
import com.record.repository.BusinessRepository;
import com.record.repository.EmailRepository;
import com.record.repository.LobRepository;
import com.record.util.MapperUtil;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private BusinessRepository businessRepository;

	private EmailRepository emailRepo;
	private LobRepository lobRepository;

	public EmailService(EmailRepository emailRepo, LobRepository lobRepository) {
		this.emailRepo = emailRepo;
		this.lobRepository = lobRepository;
	}

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("noreply@baeldung.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}

	public EmailModel createEmail(EmailModel req) throws BadRequestException {
		EmailEntity emailEntity = MapperUtil.convertModelToEntityStrict(req, EmailEntity.class);

		Optional<BusinessEntityOp> businessEntity = businessRepository
				.findByEntityId(req.getBusinessModel().getEntityId());
		if (businessEntity.isPresent()) {
			emailEntity.setBusinessEntity(businessEntity.get());
		} else {
			throw new BadRequestException("Business Entity not exists!");
		}

		if (emailRepo.findByEmail(emailEntity.getEmail()).isPresent()) {
			throw new BadRequestException("Email " + emailEntity.getEmail() + " should be unique");
		}

		if (req.getLobCode() != null && req.getLobCode() != "") {
			List<String> lobCodeList = Stream.of(req.getLobCode().split(",")).collect(Collectors.toList());

			if (!lobCodeList.isEmpty()) {
				lobCodeList.stream().forEach(lobCode -> {
					Optional<LobEntity> lobEntity = lobRepository.findByLobCode(lobCode);
					if (!lobEntity.isPresent()) {
						throw new EntityNotFoundException("LOB Code " + lobCode + " not found!");
					}
				});
			} else {
				throw new BadRequestException("Lob List Can not be empty!");
			}
		} else {
			throw new BadRequestException("Lob List Can not be empty!");
		}

		emailEntity.setLastOpIndicator(LastOpIndicator.I);
		return MapperUtil.convertEntityToModel(emailRepo.save(emailEntity), EmailModel.class);
	}

	public EmailModel updateEmail(Long emailId, EmailModel emailModel)
			throws EntityNotFoundException, BadRequestException {

		EmailEntity emailEntity = findEmailById(emailId);
		emailEntity.setSalutation(emailModel.getSalutation());
		emailEntity.setClaim(emailModel.getClaim());
		emailEntity.setAccounting(emailModel.getAccounting());
		emailEntity.setUnderwriting(emailModel.getUnderwriting());
		emailEntity.setExpireDate(emailModel.getExpireDate());

		Optional<BusinessEntityOp> businessEntity = businessRepository
				.findByEntityId(emailModel.getBusinessModel().getEntityId());
		if (businessEntity.isPresent()) {
			emailEntity.setBusinessEntity(businessEntity.get());
		} else {
			throw new EntityNotFoundException("Business Entity not exists!");
		}

		if (emailModel.getLobCode() != null && emailModel.getLobCode() != "") {
			List<String> lobCodeList = Stream.of(emailModel.getLobCode().split(",")).collect(Collectors.toList());

			if (!lobCodeList.isEmpty()) {
				lobCodeList.stream().forEach(lobCode -> {
					Optional<LobEntity> lobEntity = lobRepository.findByLobCode(lobCode);
					if (!lobEntity.isPresent()) {
						throw new EntityNotFoundException("LOB Code " + lobCode + " not found!");
					}
				});
			} else {
				throw new BadRequestException("Lob List Can not be empty!");
			}
		} else {
			throw new BadRequestException("Lob List Can not be empty!");
		}
		emailEntity.setLobCode(emailModel.getLobCode());
		emailEntity.setLastOpIndicator(LastOpIndicator.U);
		return MapperUtil.convertEntityToModel(emailRepo.save(emailEntity), EmailModel.class);
	}

	public EmailEntity findEmailById(Long id) throws EntityNotFoundException {
		Optional<EmailEntity> emailEntity = emailRepo.findById(id);
		if (!emailEntity.isPresent()) {
			throw new EntityNotFoundException("Email with Id " + id + " not found!");
		}
		return emailEntity.get();
	}

	public List<EmailModel> getAllByStatus(String status) {

		if (status.equalsIgnoreCase("all")) {
			return emailRepo.findAll().stream()
					.map(emailEntity -> MapperUtil.convertEntityToModel(emailEntity, EmailModel.class))
					.collect(Collectors.toList());
		} else if (status.equalsIgnoreCase("true")) {
			List<EmailEntity> entitiesList = emailRepo.findByStatusTrue();
			if(entitiesList.isEmpty()) {
				throw new ResourceNotFoundException("Email not found for status "+status);
			}
			return entitiesList.stream()
					.map(emailEntity -> MapperUtil.convertEntityToModel(emailEntity, EmailModel.class))
					.collect(Collectors.toList());
		} else {
			List<EmailEntity> entitiesList = emailRepo.findByStatusFalse();
			if(entitiesList.isEmpty()) {
				throw new ResourceNotFoundException("Email not found for status "+status);
			}
			return entitiesList.stream()
					.map(emailEntity -> MapperUtil.convertEntityToModel(emailEntity, EmailModel.class))
					.collect(Collectors.toList());

		}

	}

	public EmailModel findByEmailId(Long id) {

		return MapperUtil.convertEntityToModel(findEmailById(id), EmailModel.class);

	}

	public void deleteEmail(Long emailId) {
		EmailEntity emailEntity = findEmailById(emailId);
		if (emailEntity != null) {
			emailEntity.setLastOpIndicator(LastOpIndicator.D);
			emailEntity.setStatus(false);
			emailRepo.save(emailEntity);
		}
	}

	public List<EmailModel> findEmailByEntityId(String id) {

		Optional<BusinessEntityOp> businessEntity = businessRepository.findByEntityId(id);
		if (businessEntity.isPresent()) {
			List<EmailEntity> emailList = emailRepo.findByBusinessEntity(businessEntity.get());
			if(emailList.isEmpty()) {
				throw new EntityNotFoundException("No Email exist for entityId "+id);
			}
			return emailList.stream()
					.map(emailEntity -> MapperUtil.convertEntityToModel(emailEntity, EmailModel.class))
					.collect(Collectors.toList());
		} else {
			throw new EntityNotFoundException("Business Entity not exists!");
		}

	}
	
	public List<EmailModel> findEmailByBusiessId(Long id) {

		Optional<BusinessEntityOp> businessEntity = businessRepository.findById(id);
		if (businessEntity.isPresent()) {
			List<EmailEntity> emailList = emailRepo.findByBusinessEntity(businessEntity.get());
			if(emailList.isEmpty()) {
				throw new EntityNotFoundException("No Email exist for entityId "+id);
			}
			return emailList.stream()
					.map(emailEntity -> MapperUtil.convertEntityToModel(emailEntity, EmailModel.class))
					.collect(Collectors.toList());
		} else {
			throw new EntityNotFoundException("Business Entity not exists!");
		}

	}

	public List<EmailModel> getAllEntity() {

		List<EmailEntity> emailList = emailRepo.findAll();
		if(emailList.isEmpty()) {
			throw new EntityNotFoundException("No Emails Found");
		}
		return emailList.stream()
				.map(emailEntity -> MapperUtil.convertEntityToModel(emailEntity, EmailModel.class))
				.collect(Collectors.toList());

	}

}
