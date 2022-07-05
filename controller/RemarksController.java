package com.record.controller;

import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;
import static com.record.util.Constants.REMARKS;

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
import com.record.model.RemarksModel;
import com.record.service.RemarkService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(REMARKS)
@Api(value = "Remarks Controller", tags = { "remarks" })
public class RemarksController {


	@Autowired
	public RemarkService remarkService;
	
	@ApiOperation(value = "Create remarks in system", nickname = "CreateRemarks", notes = "Create remark for Record Admin", response = RemarksModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "remarks" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RemarksModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<RemarksModel> create(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create role", required = true) @Valid @RequestBody RemarksModel remarksModel)
			throws BadRequestException {
		return new ResponseEntity<>(remarkService.createRemarks(remarksModel), HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Retrive remarks in system", nickname = "RetrieveRemarks", notes = "Retrive remark for Record Admin", response = RemarksModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "remarks" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RemarksModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/{remarkId}")
	public ResponseEntity<RemarksModel> getRemarks(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable("remarkId") Long remarkId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(remarkService.findByRemarkId(remarkId), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrive remarks in system", nickname = "RetrieveRemarks", notes = "Retrive remark for Record Admin", response = RemarksModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "remarks" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RemarksModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("byRemarksCode/{remarksCode}")
	public ResponseEntity<RemarksModel> getRemarksByCode(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable("remarksCode") String code,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(remarkService.findByRemarkCode(code), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrive remarks in system", nickname = "CreateRemarks", notes = "Create remark for Record Admin", response = RemarksModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "remarks" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RemarksModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping("/{remarkId}")
	public ResponseEntity<RemarksModel> updateRemarks(
			@ApiParam(value = "Identifier of Role to be updated") @PathVariable("remarkId") Long remarkId,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody RemarksModel remarksModel,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(remarkService.updateRemarks(remarkId, remarksModel), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrive remarks in system", nickname = "CreateRemarks", notes = "Create remark for Record Admin", response = RemarksModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "remarks" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RemarksModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping("byRemarksCode/{remarkCode}")
	public ResponseEntity<RemarksModel> updateRemarksByCode(
			@ApiParam(value = "Identifier of Role to be updated") @PathVariable("remarkCode") String remarkCode,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody RemarksModel remarksModel,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(remarkService.updateRemarksByCode(remarkCode, remarksModel), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete remarks in system", nickname = "DeleteRemarks", notes = "Delete remark for Record Admin", response = RemarksModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "remarks" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RemarksModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping("/{remarkId}")
	public ResponseEntity<String> deleteRemarks(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable("remarkId") Long remarkId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		remarkService.deleteRemarks(remarkId);
		return ResponseEntity.ok("Attachment Deleted Successfully");
	}
	
	@ApiOperation(value = "Delete remarks in system", nickname = "DeleteRemarks", notes = "Delete remark for Record Admin", response = RemarksModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "remarks" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RemarksModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping("byRemarksCode/{remarkCode}")
	public ResponseEntity<String> deleteRemarksByCode(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable("remarkCode") String remarkCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		remarkService.deleteRemarksByCode(remarkCode);
		return ResponseEntity.ok("Attachment Deleted Successfully");
	}
	
}
