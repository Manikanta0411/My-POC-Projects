package com.record.controller;
import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.record.dto.AttachmentListDto;
import com.record.exception.BadRequestException;
import com.record.exception.InvalidFileTypeException;
import com.record.model.Attachment;
import com.record.service.AttachmentService;
import com.record.util.FileDownloadUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@Api(value = "Attachment Controller", tags = { "Attachment" })
public class AttachmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentController.class);

    @Autowired
    private AttachmentService attachmentService;

    @Value("#{'${allowed.file.types}'.split(',')}")
    private List<String> allowedFileTypes;

    @ApiOperation(value = "Add Attachment", nickname = "addAttachment", notes = "Add Attachment for Entity Admin", response = AttachmentListDto.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "Attachment" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = AttachmentListDto.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @PostMapping("layer/{layerId}/file")
    public ResponseEntity<String> uploadFile(@PathVariable(name = "layerId") Long layerId,
                                             @RequestParam("file") MultipartFile file,
                                             @ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) throws IOException {
        try {
            Blob blob = new SerialBlob(file.getBytes());
            Attachment attachment = new Attachment();
            String fileName = file.getOriginalFilename();
            attachment.setFileName(fileName);
            attachment.setType(StringUtils.getFilenameExtension(fileName));
            attachment.setBytes(file.getBytes());
            validateFileType(file);
            attachment.setSize(blob.length());
            attachmentService.addAttachment(attachment, layerId);
        } catch (Exception ex) {
            LOGGER.error("Error when adding attachment " + ex);
            throw new IOException("Error when adding an attachment ");
        }
        return ResponseEntity.ok("SUCCESS");
    }
    
	@ApiOperation(value = "Add Attachment", nickname = "addAttachment", notes = "add Attachment for  Entity Admin", response = AttachmentListDto.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "Attachment" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = AttachmentListDto.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @PostMapping("layer/{layerId}/file/{fileId}")
    public ResponseEntity assignEntities(@RequestBody AttachmentListDto attachmentListDto,
    		@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) throws IOException {

        attachmentListDto.getAttachments().forEach(attachmentDto ->
            attachmentService.assignEntitiesToAttachment(attachmentDto.getId(), attachmentDto.getBusinessEntityIds())
        );
        return new ResponseEntity(HttpStatus.CREATED);
    }

	@ApiOperation(value = "Delete Attachment", nickname = "deleteAttachment", notes = "delete Attachment for  Entity Admin", response = AttachmentListDto.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "Attachment" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = AttachmentListDto.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @DeleteMapping("attachment/{attachmentId}")
    public ResponseEntity<String> deleteAttachment(@PathVariable(name = "attachmentId") Long attachmentId,
    		@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) throws BadRequestException {
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.ok("Attachment Deleted Successfully");
    }

    private void validateFileType(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String[] split = filename.split("\\.");
        if (split.length <= 1) {
            throw new InvalidFileTypeException("Error! Invalid file type");
        } else if (!allowedFileTypes.contains(split[1].toUpperCase())) {
            throw new InvalidFileTypeException("Error! Invalid file extension : " + split[1]);
        }
    }
    

	@GetMapping("/attachment/{attachmentId}") public ResponseEntity<Attachment> getAttachment( @ApiParam(value = "Identifier of Lead to be updated") @PathVariable(name="attachmentId") Long
	  attachmentId)  throws EntityNotFoundException {
	 
		return new ResponseEntity<Attachment>(attachmentService.getAttachementById(attachmentId), HttpStatus.OK); 
	  
	}

	@GetMapping("/downloadFile/{attachmentId}")
	public ResponseEntity<?> downloadFile(@PathVariable("attachmentId") Long attachmentId) {
		FileDownloadUtil downloadUtil = new FileDownloadUtil();
		Attachment attachement = attachmentService.getAttachementById(attachmentId);
		Resource resource = null;
		
			try {
				resource = downloadUtil.getFileAsResource(attachement);
			} catch (IOException e) {

			}
		
		if (resource == null) {
			return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
		}
		String contentType = "application/octet-stream";
		String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
	}
    
}
