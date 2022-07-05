package com.record.controller;


import static com.record.util.Constants.LOB;
import static com.record.util.Constants.LOBS_CODE;
import static com.record.util.Constants.LOBS_ID;
import static com.record.util.Constants.LOB_CODE;
import static com.record.util.Constants.LOB_ID;
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
import com.record.model.LobModel;
import com.record.service.LobService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(LOB)
@Api(value = "Lob Controller", tags = { "lob" })
public class LobController {
	@Autowired
	private LobService lobService;

	@ApiOperation(value = "Create lob in system", nickname = "CreateLob", notes = "Create LOB for Record Admin", response = LobModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "lob" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = LobModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<LobModel> create(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create lob", required = true) @Valid @RequestBody LobModel lob)
			throws BadRequestException { 
		return new ResponseEntity<>(lobService.createLob(lob), HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Update lob", nickname = "updateLob", notes = "Update Lob for  Record Admin", response = LobModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "lob" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = LobModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping(LOB_ID)
	public ResponseEntity<LobModel> update(
			@ApiParam(value = "Identifier of Role to be updated") @PathVariable(LOBS_ID) Long lobId,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody LobModel lob,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException, BadRequestException {
		return new ResponseEntity<>(lobService.updateLob(lobId, lob), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete Lob", nickname = "DeleteLob ", notes = "Delete Lob from Record Admin", response = LobModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "lob" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = LobModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping(LOB_ID)
	public ResponseEntity<Void> deletelob(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable(LOBS_ID) Long lobId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		lobService.deleteLob(lobId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "Retrieve Lobs", nickname = "Retrieve Lobs ", notes = "Retrieve Lobs from Record Admin", response = LobModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "lob" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = LobModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/byStatus/{status}")
	public ResponseEntity<List<LobModel>> getLobs(
			@ApiParam(value = "User Token issued during login call") @PathVariable("status") String status, @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		return new ResponseEntity<>(lobService.getAllLobs(status), HttpStatus.OK);
	}
	
	@GetMapping(LOB_CODE)
	public ResponseEntity<LobModel> getRoleByCode(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable(LOBS_CODE) String lobCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(lobService.findBylobCode(lobCode), HttpStatus.OK);
	}
	
	
}
