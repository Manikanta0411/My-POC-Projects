package com.record.controller;

import static com.record.util.Constants.EMAIL;
import static com.record.util.Constants.EMAILS_ID;
import static com.record.util.Constants.EMAIL_ID;
import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.exception.BadRequestException;
import com.record.model.ActionModel;
import com.record.model.EmailModel;
import com.record.service.EmailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(EMAIL)
@Api(value = "email Entity Controller", tags = { "email" })
public class EmailEntityController {
	@Autowired
	private EmailService emailService;

	@ApiOperation(value = "Create email entity in system", nickname = "CreateEmail", notes = "Create email entity for Record Admin", response = EmailModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "email" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = EmailModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<EmailModel> create(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create lob", required = true) @Valid @RequestBody EmailModel email)
			throws BadRequestException { 
		return new ResponseEntity<>(emailService.createEmail(email), HttpStatus.CREATED);
	}
	
	
	@ApiOperation(value = "Update email Entity", nickname = "updateemailEntity", notes = "Update email Entity for  Record Admin", response = EmailModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "email" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = EmailModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping(EMAIL_ID)
	public ResponseEntity<EmailModel> update(
			@ApiParam(value = "Identifier of Role to be updated") @PathVariable(EMAILS_ID) Long id,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody EmailModel emailReq,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException, BadRequestException {
		return new ResponseEntity<>(emailService.updateEmail(id, emailReq), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Retrieve email Entity by status", nickname = "Retrieve email Entity by status", notes = "Retrieve email Entity from Record Admin", response = EmailModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "email" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = EmailModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/byStatus/{status}")
	public ResponseEntity<List<EmailModel>> getEmails(
			@ApiParam(value = "User Token issued during login call") @PathVariable("status") String status, @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) 
	{
		return new ResponseEntity<>(emailService.getAllByStatus(status), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieve email Entity by id", nickname = "Retrieve email Entity by id", notes = "Retrieve email Entity from Record Admin", response = EmailModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "email" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = EmailModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping(EMAIL_ID)
	public ResponseEntity<EmailModel> getEmailById(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable(EMAILS_ID) Long emailId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(emailService.findByEmailId(emailId), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete Email", nickname = "Delete Email ", notes = "Delete Email from Record Admin", response = ActionModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "email" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = ActionModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping(EMAIL_ID)
	public ResponseEntity<String> deleteAction(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable(EMAILS_ID) Long emailId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		emailService.deleteEmail(emailId);
		return ResponseEntity.ok("Deleted Successfully");
	}

	@ApiOperation(value = "Retrieve email Entity by entity Id", nickname = "Retrieve email Entity by entity Id", notes = "Retrieve email Entity from Record Admin", response = EmailModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "email" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = EmailModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/byEntityId/{entityId}")
	public ResponseEntity<List<EmailModel>> getEmailByEntityId(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable("entityId") String id,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(emailService.findEmailByEntityId(id), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieve all emails", nickname = "Retrieve All emails", notes = "Retrieve email Entity from Record Admin", response = EmailModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "email" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = EmailModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping()
	public ResponseEntity<List<EmailModel>> getAllEntity(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(emailService.getAllEntity(), HttpStatus.OK);
	}
	
	
	
}
