package com.record.service;

<<<<<<< HEAD
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

=======
>>>>>>> branch 'Mani_work_June8th2022' of https://github.com/RE-cord/Record_Backend_API.git
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.record.entity.BusinessEntityOp;
<<<<<<< HEAD
import com.record.model.EmailModel;
import com.record.model.Layer;
import com.record.model.Mail;

import freemarker.template.Configuration;
=======
import com.record.model.Layer;
>>>>>>> branch 'Mani_work_June8th2022' of https://github.com/RE-cord/Record_Backend_API.git

@Service
public class MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Autowired
    @Qualifier("mailSender")
    private JavaMailSender mailSender;

<<<<<<< HEAD
	@Autowired
	private EmailService emailService;
=======
    public void sendEmail(Layer layer, BusinessEntityOp entity) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
>>>>>>> branch 'Mani_work_June8th2022' of https://github.com/RE-cord/Record_Backend_API.git

	@Autowired
	Configuration fmConfiguration;

	@Value("classpath:Logo.jpg")
	Resource resourceFile;

	public void sendEmail(Layer layer, BusinessEntityOp entity, File file) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {

			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			List<EmailModel> emailList = emailService.findEmailByBusiessId(entity.getId());

			mimeMessageHelper.setSubject(layer.getName());
			mimeMessageHelper.setFrom("manikanta149000@gmail.com");

//			for (EmailModel emailModel : emailList) {
//				mimeMessageHelper.addTo(emailModel.getEmail());
//			}

			emailList.stream().forEach(emailModel -> {
				try {
					mimeMessageHelper.addTo(emailModel.getEmail());
				} catch (MessagingException e) {
					LOGGER.error("Error when sending the email to entity " + entity.getId(), e);
				}
			});
			Mail mail = new Mail();
			mail.setMailSubject("Spring 5 - Email with freemarker template");

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("firstName", "hello");
			model.put("lastName", "hello");
			model.put("location", "Hyderabad");
			model.put("signature", "www.record.com");
			mail.setModel(model);

	//		mimeMessageHelper.setTo("chouhan.shikhar@recordreinsure.com");
			mimeMessageHelper.setSubject(mail.getMailSubject());
			mail.setMailContent(getContentFromTemplate(mail.getModel()));
			mimeMessageHelper.setText(mail.getMailContent(), true);
			// mimeMessageHelper.setText("Layer details has been created/updated. Layer id :
			// " + layer.getId());
			mimeMessageHelper.addInline("attachment.png", resourceFile);
			mimeMessageHelper.addAttachment(file.getName(), file);
			mailSender.send(mimeMessageHelper.getMimeMessage());

		} catch (MessagingException e) {
			LOGGER.error("Error when sending the email to entity " + entity.getId(), e);
		}
	}

	public String getContentFromTemplate(Map<String, Object> model) {
		StringBuffer content = new StringBuffer();
		try {
			// content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
			// "/templates/email-template.vm", model));

			content.append(FreeMarkerTemplateUtils
					.processTemplateIntoString(fmConfiguration.getTemplate("email-template.vm"), model));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}
}
